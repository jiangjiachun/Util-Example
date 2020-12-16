package com.jjc.snmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.UdpAddress;

import com.jjc.excel.ExcelExport;
import com.jjc.hex.HexConversion;

public class TrapTest {

    private static final Logger logger = LoggerFactory.getLogger(HexConversion.class);
    
    @Ignore
    @Test
    public void listen() {
        SnmpTrapReceiver snmpTrapReceiver = new SnmpTrapReceiver();
        try {
            snmpTrapReceiver.listen(new UdpAddress("127.0.0.1/162"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void excel() {
        try {
            File file = new File(HexConversion.class.getResource("/").toURI().getPath() + "snmp/Trap监听日志.txt");
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader buffReader = new BufferedReader(reader);
            String strTmp = "";
            List<Object[]> datas = new ArrayList<>();
            while((strTmp = buffReader.readLine()) != null){
                if(strTmp.startsWith("Oid = 1.3.6.1.4.1.112552.1.1.1.2.0")) {
                    strTmp = strTmp.toLowerCase().split("value =")[1].trim();
                    String str = HexConversion.hexGBK2String(strTmp.replace(":", ""));
                    logger.info("十六进制转换结果={}", str);
                    String[] row = str.substring(1, str.length() - 1).split("\\|");
                    row = Arrays.copyOf(row, row.length + 3);
                    row[row.length - 3] = "1.3.6.1.4.1.112552.1.1.1.2.0";
                    row[row.length - 2] = strTmp;
                    row[row.length - 1] = str;
                    // 事件类型
                    String funType = row[4];
                    switch (funType) {
                        case "0":
                            row[4] = "通讯出错";
                            break;
                        case "1":
                            row[4] = "采集值正常，通讯恢复正常";
                            break;
                        case "2":
                            row[4] = "高值报警，即模拟量中上限报警";
                            break;
                        case "3":
                            row[4] = "不正常值";
                            break;
                        case "4":
                            row[4] = "低值报警，与上高值报警呼应";
                            break;
                        case "5":
                            row[4] = "错误数据/无效数据";
                            break;
                        case "6":
                            row[4] = "设置失败";
                            break;
                        case "7":
                            row[4] = "事件报警";
                            break;
                        case "10":
                            row[4] = "策略0-1事件";
                            break;
                        case "12":
                            row[4] = "策略1-0事件";
                            break;
                        case "19":
                            row[4] = "确认事件";
                            break;
                        case "13":
                            row[4] = "设置成功";
                            break;
                        case "14":
                            row[4] = "正在设置";
                            break;
                        case "15":
                            row[4] = "有设置请求";
                            break;
                        case "16":
                            row[4] = "已取消的点";
                            break;
                        case "18":
                            row[4] = "尚未采集数据";
                            break;
                        default:
                            break;
                    }
                    datas.add(row);
                }
            }
            buffReader.close();
            String[] header = {"站点名", "设备名", "事件描述", "事件等级", "事件类型", "发生时间", "事件ID", "设备编号", "OID", "HEX", "HEX转换结果"};
            ExcelExport.export("Trap", header, datas, new File("d://trap.xls"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
