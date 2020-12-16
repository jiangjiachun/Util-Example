package com.jjc.snmp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

public class SnmpTrapReceiver implements CommandResponder {
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public synchronized void listen(TransportIpAddress address) throws IOException {
        AbstractTransportMapping<?> transportMapping;
        if(address instanceof TcpAddress) {
            transportMapping = new DefaultTcpTransportMapping((TcpAddress)address);
        }
        else {
            transportMapping = new DefaultUdpTransportMapping((UdpAddress)address);
        }
        
        ThreadPool threadPool = ThreadPool.create("DispatcherPool", 10);
        MessageDispatcher dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
        
        dispatcher.addMessageProcessingModel(new MPv1());
        dispatcher.addMessageProcessingModel(new MPv2c());
        
        SecurityProtocols.getInstance().addDefaultProtocols();
        SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());
        
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        
        Snmp snmp = new Snmp(dispatcher, transportMapping);
        snmp.addCommandResponder(this);
        
        transportMapping.listen();
        System.out.println("Listening on " + address);
        
        try {
            this.wait();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public synchronized void processPdu(CommandResponderEvent event) {
        System.out.println("Received PDU...");
        PDU pdu = event.getPDU();
        
        if(pdu != null) {
            System.out.println("Trap Type = " + pdu.getType() + ", Listen Time = " + sdf.format(new Date()));
            pdu.getVariableBindings().forEach(variableBinding -> {
                System.out.println("Oid = " + variableBinding.getOid() + ",Value = " + variableBinding.getVariable().toString());
            });
        }
    }
    
    
    public static void main(String[] args) {
        SnmpTrapReceiver snmpTrapReceiver = new SnmpTrapReceiver();
        try {
            snmpTrapReceiver.listen(new UdpAddress("127.0.0.1/162"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
