package uk.org.vacuumtube.commons.http;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author clivem
 *
 */
public class ProxyDetect {
    
    private static final Logger logger = Logger.getLogger(ProxyDetect.class);
    /*
    private static String proxyIP = "";
    private static int proxyPort = 80;
    // If SSL version, assume autoDetect, else do not
    private static boolean autoDetectProxy = false;  
    private static boolean useProxy = false;
    */
     
    private ProxyDetect() {
    }
    
    public static boolean detect(String location) {
        String javaVers = System.getProperty("java.version");
        if (javaVers.startsWith("1.5"))  {
            return detect5(location);
        } else {
        	return false;
            //return detect4(location);
        }
    }
    
    /*
    public static void detect_() {
        sun.plugin.net.proxy.ProxyInfo info = sun.plugin.net.proxy.PluginProxyManager.getProxyInfo(new URL(location));
        if (info != null) {
            logger.info("Setting proxyHost/port from ProxyService");
            System.setProperty("http.proxyHost", info.getProxy());
            System.setProperty("http.proxyPort", String.valueOf(info.getPort()));
        }
    }
    */
    
    /*
    public static boolean detect4(String location) {
        try {
            ProxyInfo info[] = ProxyService.getProxyInfo(new URL(location));
            if(info != null && info.length > 0) {
                logger.info("PROXY = " + info[0].getHost() + ":" + info[0].getPort());
            }
        } catch (Exception ex) {
            logger.error("Could not retrieve proxy configuration: " + ex.toString());
        }
        return false;
    } 
    */   
    
    public static boolean detect5(String location) {
        try {
            logger.info("Detecting system proxy settings");
            System.setProperty("java.net.useSystemProxies", "true");
            ProxySelector proxySelector = ProxySelector.getDefault();
            logger.info(proxySelector.getClass().getName());
            if (proxySelector != null) {
                List<Proxy> list = proxySelector.select(new URI(location));
                Iterator<Proxy> it = list.iterator();
                while (it.hasNext()) {
                    Proxy proxy = (Proxy) it.next();
                    logger.info(proxy);
                    Proxy.Type proxyType = proxy.type();
                    if (proxyType == Proxy.Type.HTTP) {
                        InetSocketAddress sa = (InetSocketAddress) proxy.address();
                        logger.info("Setting proxy: " + sa);
                        System.setProperty("http.proxyHost", sa.getHostName());
                        System.setProperty("https.proxyHost", sa.getHostName());
                        System.setProperty("http.proxyPort", String.valueOf(sa.getPort()));
                        System.setProperty("https.proxyPort", String.valueOf(sa.getPort()));
                        return true;
                    }
                }
            }
        //} catch(NoClassDefFoundError ncdfe) {
        	
        } catch (Exception e) {
            logger.error("Could not retrieve system proxy configuration: " + e.toString());
        }
        return false;
    }
    
    /*
    public static void detectProxy(URL sampleURL) {
        String javaVers = System.getProperty("java.version");
        // Added in 5.0.3 - Plugin 1.4 will hide the internal Sun proxy stuff,
        // so use new proxy property introduced in 1.3.0_1 (1.3.1)...
        // 5.0.7 Update: - Plugin 1.4 Final added com.sun.java.browser.net.*
        // classes ProxyInfo & ProxyService... Use those with JREs => 1.4
        System.out.println("About to attempt auto proxy detection under Java version:" + javaVers);
        boolean invokeFailover = false; // If specific, known detection methods
                                        // fail may try fallback detection
                                        // method
     
        if (javaVers.startsWith("1.4"))  {
            try {
                //  Look around for the 1.4.X plugin proxy detection class...
                // Without it, cannot autodetect...
                Class t = Class.forName("com.sun.java.browser.net.ProxyService");
                com.sun.java.browser.net.ProxyInfo[] pi = com.sun.java.browser.net.ProxyService.getProxyInfo(sampleURL);
                if (pi == null || pi.length == 0) {
                    System.out.println("1.4.X reported NULL proxy (no proxy assumed)");
                    useProxy = false;
                }
                else {
                    System.out.println("1.4.X Proxy info geProxy:"+pi[0].getHost()+ " get Port:"+pi[0].getPort()+" isSocks:"+pi[0].isSocks());
                    useProxy = true;
                    proxyIP = pi[0].getHost();
                    proxyPort = pi[0].getPort();
                    System.out.println("proxy " + proxyIP+" port " + proxyPort);
                }
            }
            catch (Exception ee) {
                System.out.println("Sun Plugin 1.4.X proxy detection class not found, will try failover detection, e:"+ee);
                invokeFailover = true;
            }
        }
        else {
            System.out.println("Sun Plugin reported java version not 1.3.X or 1.4.X, trying failover detection...");
            invokeFailover = true;
        }
        if (invokeFailover) {
            System.out.println("Using failover proxy detection...");
             try {
                String proxyList = ((String)System.getProperties().getProperty("javaplugin.proxy.config.list")).toUpperCase();
                System.out.println("Plugin Proxy Config List Property:"+proxyList);
                useProxy = (proxyList != null);
                if (useProxy) {     //  Using HTTP proxy as proxy for HTTP proxy
                                    // tunnelled SSL socket (should be listed
                                    // FIRST)....
                    // 6.0.0 1/14/03 1.3.1_06 appears to omit HTTP portion of
                    // reported proxy list... Mod to accomodate this...
                    // Expecting proxyList of "HTTP=XXX.XXX.XXX.XXX:Port" OR
                    // "XXX.XXX.XXX.XXX:Port" & assuming HTTP...
                    if (proxyList.indexOf("HTTP=") > -1)
                         proxyIP = proxyList.substring(proxyList.indexOf("HTTP=")+5, proxyList.indexOf(":"));
                    else proxyIP = proxyList.substring(0, proxyList.indexOf(":"));
                    int endOfPort = proxyList.indexOf(",");
                    if (endOfPort < 1) endOfPort = proxyList.length();
                    proxyPort = Integer.parseInt(proxyList.substring(proxyList.indexOf(":")+1,endOfPort));
                    System.out.println("proxy " + proxyIP+" port " + proxyPort);
                }
            }
            catch (Exception e) {
                System.out.println("Exception during failover auto proxy detection, autoDetect disabled, e:"+e);
                autoDetectProxy = false;
            }
        }
    }
    */
}
