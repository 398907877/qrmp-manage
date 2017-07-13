package com.yanyan.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetInputListener;
import org.apache.commons.net.telnet.TelnetNotificationHandler;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;
import org.apache.commons.net.telnet.WindowSizeOptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: Telnet代理工具
 *
 * @author Saintcy Don
 * @version 1.0
 */
public class TelnetAgent implements TelnetNotificationHandler, TelnetInputListener {
    private static final Logger logger = LoggerFactory.getLogger(TelnetAgent.class.getName());
    private static Boolean checking = true;
    private static final long MAX_INACTIVE_INTERVAL = 30 * 60 * 1000;// 30min
    private static ConcurrentHashMap<String, TelnetAgent> agents = new ConcurrentHashMap<String, TelnetAgent>();

    private static final class ActivityCheckerThread extends Thread {
        public ActivityCheckerThread() {
            start();
        }

        public void run() {
            logger.info("ActivityCheckerThread [" + this.getName() + "] Start");
            while (checking) {
                //logger.info("======>" + this.isInterrupted());
                try {
                    Thread.sleep(10000);
                    long currentTime = System.currentTimeMillis();
                    if (agents == null)
                        break;
                    for (Entry<String, TelnetAgent> entry : agents.entrySet()) {
                        String id = entry.getKey();
                        TelnetAgent ta = entry.getValue();
                        if (ta.getLastAccessedTime() <= currentTime - MAX_INACTIVE_INTERVAL) {
                            agents.remove(id);
                            ta.disconnect();
                            ta = null;
                            logger.info("Telnet agent " + id + " expired");
                        }
                    }
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                    break;
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
            logger.info("ActivityCheckerThread [" + this.getName() + "] Stoped");
        }
    }

    ;

    @SuppressWarnings("unused")
    private static final ActivityCheckerThread checker = new ActivityCheckerThread();

    private TelnetClient telnet;
    private String charset = "UTF-8";
    private long lastAccessedTime;
    private long thisAccessedTime;
    private StringBuffer buffer;
    // private static final String CTRL_C = new String(new char[] { 17, 67 });
    private Boolean writing = false;
    private PipedOutputStream out;
    private PipedInputStream in;

    public static TelnetAgent createAgent() {
        return new TelnetAgent();
    }

    public static TelnetAgent getAgent(String id) {
        return agents.get(id);
    }

    public static void putAgent(String id, TelnetAgent agent) {
        agents.put(id, agent);
    }

    private TelnetAgent() {
        buffer = new StringBuffer();
        this.out = new PipedOutputStream();
        try {
            this.in = new PipedInputStream(this.out);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private synchronized void write(String s) {
        synchronized (writing) {
            writing = true;
            buffer.append(s);
            writing = false;
        }
    }

    private synchronized String read() {
        synchronized (writing) {
            if (!writing) {
                String s = buffer.toString();
                logger.info("[RCV]" + s);
                buffer.setLength(0);
                return s;
            } else {
                return "";
            }
        }
    }

    public void connect(String ip, int port) throws SocketException, IOException {
        logger.info("Connect to " + ip + " " + port + "...");
        access();
        this.telnet = new TelnetClient();
        TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false);
        EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true, false);
        SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true, true, true);
        WindowSizeOptionHandler wsopt = new WindowSizeOptionHandler(160, 24, true, true, true, true);

        try {
            this.telnet.addOptionHandler(ttopt);
            this.telnet.addOptionHandler(echoopt);
            this.telnet.addOptionHandler(gaopt);
            this.telnet.addOptionHandler(wsopt);
        } catch (Exception e) {
            logger.error("Error registering option handlers: " + e.getMessage(), e);
        }
        this.telnet.registerNotifHandler(this);
        this.telnet.registerInputListener(this);
        try {
            this.telnet.connect(ip, port);
        } catch (Exception e) {
            logger.error("", e);
            write("连接失败:" + e.toString());
        }

        access();
    }

    public void disconnect() {
        try {
            if (telnet != null && telnet.isConnected()) {
                telnet.disconnect();
            }
        } catch (Exception e) {
            logger.error("", e);
            write("Disconnect fail: " + e.toString());
        } finally {
            if (this.in != null) {
                try {
                    this.in.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }

            if (this.out != null) {
                try {
                    this.out.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
    }

    public void acceptKeybroadInput() {
        OutputStream outstr = this.telnet.getOutputStream();

        byte[] buff = new byte[1024];
        int ret_read = 0;
        boolean end_loop = false;

        do {
            try {
                ret_read = System.in.read(buff);
                if (ret_read > 0) {

                    try {
                        outstr.write(buff, 0, ret_read);
                        outstr.flush();
                    } catch (IOException e) {
                        end_loop = true;
                    }
                }
            } catch (IOException e) {
                logger.error("Exception while reading keyboard: " + e.getMessage(), e);
                end_loop = true;
            }
        } while ((ret_read > 0) && (end_loop == false));
    }

    public String receive() {
        access();
        return read();
    }

    public synchronized void send(String command) {
        access();
        try {
      /*if (command.equals(CTRL_C)) {// CTRL+C
        command = "\u0003";
      } else {
        command += SystemUtils.LINE_SEPARATOR;
      }*/
            logger.info("[CMD]" + command);
            telnet.getOutputStream().write(command.getBytes());
            telnet.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("", e);
            write("Send command fail: " + e.toString());
        }
        access();
    }

    public void telnetInputAvailable() {
        InputStream instr = this.telnet.getInputStream();

        BufferedReader reader = null;
        try {
            byte[] buff = new byte[1];// 设>1有些机器会被阻塞
            int ret_read = 0;
            do {
                ret_read = instr.read(buff);
                if (ret_read > 0) {
                    out.write(buff, 0, ret_read);
                    if (buff[0] < 0 || buff[0] > 127)
                        return;
                }
            } while (ret_read == 1024);
            out.flush();
        } catch (Exception e) {
            logger.error("Exception while reading socket:" + e.getMessage(), e);
        }

        try {
            reader = new BufferedReader(new InputStreamReader(in, this.charset));
            if (!reader.ready())
                return;
            int ret_read = 0;
            char[] cbuf = new char[1024];
            do {
                ret_read = reader.read(cbuf);
                if (ret_read > 0) {
                    write(new String(cbuf, 0, ret_read));
                    logger.debug("[RSP]" + new String(cbuf, 0, ret_read));
                }
            } while (ret_read == 1024);
        } catch (IOException e) {
            logger.error("Exception while reading socket:" + e.getMessage(), e);
        }
    /*try {      
      byte[] buff = new byte[1024];
      int ret_read = 0;
      do {
        ret_read = instr.read(buff);
        if (ret_read > 0) {
          write(new String(buff, 0, ret_read, this.charset));
          System.out.print(new String(buff, 0, ret_read, this.charset));
        }
      } while (ret_read == 1024);
    } catch (IOException e) {
      logger.error("Exception while reading socket:" + e.getMessage(), e);
    }finally{
      
    }*/
    }

    public void setCharset(String charset) {
        if (Charset.isSupported(charset)) {
            this.charset = charset;
        } else {
            throw new IllegalArgumentException("Unsupported charset \"" + charset + "\"");
        }
    }

    public void access() {
        this.lastAccessedTime = this.thisAccessedTime;
        this.thisAccessedTime = System.currentTimeMillis();
    }

    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    public void finalize() throws Throwable {
        super.finalize();
        this.disconnect();
    }

    public void receivedNegotiation(int negotiation_code, int option_code) {
        String command = null;
        if (negotiation_code == TelnetNotificationHandler.RECEIVED_DO) {
            command = "DO";
        } else if (negotiation_code == TelnetNotificationHandler.RECEIVED_DONT) {
            command = "DONT";
        } else if (negotiation_code == TelnetNotificationHandler.RECEIVED_WILL) {
            command = "WILL";
        } else if (negotiation_code == TelnetNotificationHandler.RECEIVED_WONT) {
            command = "WONT";
        }
        logger.info("Received " + command + " for option code " + option_code);
    }

    public static void main(String[] args) throws Exception {
        TelnetAgent agent = TelnetAgent.createAgent();
        agent.setCharset("GBK");
        agent.connect("192.168.1.100", 23);
        agent.acceptKeybroadInput();
    }

}
