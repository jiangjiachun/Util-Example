package com.jjc.snmp;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class Snmp4jTest {

	private static Snmp protocol;

	public static void main(String[] args) {
		try {
			// 设定CommunityTarget
			CommunityTarget myTarget = new CommunityTarget();
			// 定义远程主机的地址
			Address deviceAdd = GenericAddress.parse("udp:34.92.136.39/161");
			// 设定远程主机的地址
			myTarget.setAddress(deviceAdd);
			// 设置snmp共同体
			myTarget.setCommunity(new OctetString("public"));
			// 设置超时重试次数
			myTarget.setRetries(2);
			// 设置超时的时间
			myTarget.setTimeout(5 * 60);
			// 设置使用的snmp版本
			myTarget.setVersion(SnmpConstants.version2c);

			// 设定采取的协议UDP
			TransportMapping<?> transport = new DefaultUdpTransportMapping();
			// 调用TransportMapping中的listen()方法，启动监听进程，接收消息，由于该监听进程是守护进程，最后应调用close()方法来释放该进程
			transport.listen();
			protocol = new Snmp(transport);
			// 创建请求pdu,获取mib
			PDU request = new PDU();
			// 调用的add方法绑定要查询的OID
			request.add(new VariableBinding(new OID("1.3.6.1.2.1.25.1.1.0")));
			request.add(new VariableBinding(new OID("1.3.6.1.2.1.1.1.0")));
			request.add(new VariableBinding(new OID("1.3.6.1.4.1.2021.4")));
			// 调用setType()方法来确定该pdu的类型
			request.setType(PDU.GET);
			// 调用 send(PDU pdu,Target target)发送pdu，返回一个ResponseEvent对象
			ResponseEvent responseEvent = protocol.send(request, myTarget);
			// 通过ResponseEvent对象来获得SNMP请求的应答pdu，方法：public PDU getResponse()
			PDU response = responseEvent.getResponse();
			// 输出
			if (response != null) {
				System.out.println("request.size()=" + request.size());
				System.out.println("response.size()=" + response.size());
				// 通过应答pdu获得mib信息（之前绑定的OID的值）
				for (int i = 0; i < response.size(); i++) {
					VariableBinding variableBinding = response.get(i);
					System.out.println(variableBinding);
				}
				// 调用close()方法释放该进程
				transport.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
