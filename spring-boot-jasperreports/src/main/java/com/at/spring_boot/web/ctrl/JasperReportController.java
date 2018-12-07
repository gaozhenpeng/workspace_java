package com.at.spring_boot.web.ctrl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.logging.log4j.message.FormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.at.spring_boot.web.vo.CompileJRXmlVO;
import com.at.spring_boot.web.vo.HelloVO;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;

@Slf4j
@RestController
@RequestMapping("/jr")
public class JasperReportController {
    /** the path prefix of the jrxml dir */
    private static final String JRXML_DIR_PREFIX = "/jrxmls";
    /** the path prefix of the jasper dir */
    private static final String JASPER_DIR_PREFIX = "/jaspers";
    
    /** JasperReport Empty DataSource */
    private static final JRDataSource JR_EMPTY_DATASOURCE = new JREmptyDataSource();

    /** spring datasource */
    @Autowired
    private DataSource springDataSource;
    
    /**
     * 1. health check
     *   * http://localhost:8080/jrdemo/jr/hello
     * @return
     */
    @RequestMapping("/hello")
    public HelloVO hello() {
        return new HelloVO("hello jasperreport.");
    }
    
    /**
     * 1. compile jrxml file
     *   * http://localhost:8080/jrdemo/jr/cpjrxml
     *   * http://localhost:8080/jrdemo/jr/cpjrxml?jrxml=Blank_A4_helloworld
     *   * http://localhost:8080/jrdemo/jr/cpjrxml?jrxml=Blank_A4
     *   
     * @param jrxmlFilename
     * @return
     */
    @RequestMapping("/cpjrxml")
    public CompileJRXmlVO compileJrXml(@RequestParam(name = "jrxml", defaultValue = "Blank_A4_helloworld") String jrxmlFilename) {
        CompileJRXmlVO compileJRXmlVO = new CompileJRXmlVO();
        StringBuilder msgsb = new StringBuilder();
        URL jrxmlUrl = getClass().getResource(JRXML_DIR_PREFIX + "/" + jrxmlFilename + ".jrxml");
        InputStream jrxmlInputStream = null;
        try {
            // open file
            jrxmlInputStream = jrxmlUrl.openStream();
            // compile jrxml
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);
            // save jr object to file
            String jrxmlParentDirPath = jrxmlUrl.toURI().resolve("..").getPath();
            String jasperDirPath = jrxmlParentDirPath.replaceFirst("/$", "") + JASPER_DIR_PREFIX;
            File jasperDirFile = new File(jasperDirPath);
            if(!jasperDirFile.exists()) {
                jasperDirFile.mkdirs();
            }
            String jasperFilePath = jasperDirPath + "/" + jrxmlFilename + ".jasper";
            log.info("jasperFilePath: '{}'", jasperFilePath);
            JRSaver.saveObject(jasperReport, jasperFilePath);
            

            String msg = new FormattedMessage("jasper compile of '{}' ok.", jrxmlFilename).getFormattedMessage();
            msgsb.append(msg);
            log.info(msg);
        } catch (JRException e) {
            String msg = new FormattedMessage("jasper compile of '{}' failed.", jrxmlFilename, e).getFormattedMessage();
            msgsb.append(msg);
            log.error(msg);
        } catch (IOException e) {
            String msg = new FormattedMessage("open stream of jrxml file '{}' failed.", jrxmlFilename, e).getFormattedMessage();
            msgsb.append(msg);
            log.error(msg);
        } catch (URISyntaxException e) {
            String msg = new FormattedMessage("URL to URI, url path: '{}' failed.", jrxmlUrl.getPath(), e).getFormattedMessage();
            msgsb.append(msg);
            log.error(msg);
        }finally {
            if(jrxmlInputStream != null) {
                try {
                    jrxmlInputStream.close();
                } catch (IOException e) {
                    String msg = new FormattedMessage("Close stream of jrxml file '{}' failed.", jrxmlFilename, e).getFormattedMessage();
                    msgsb.append(msg);
                    log.error(msg);
                }
            }
        }
        compileJRXmlVO.setMsg(msgsb.toString());
        return compileJRXmlVO;
    }

    /**
     * 1. export pdf with empty jr connection
     *   * http://localhost:8080/jrdemo/jr/exportpdf?jasper=Blank_A4_helloworld
     * 1. export pdf with db connection
     *   * http://localhost:8080/jrdemo/jr/exportpdf?jasper=Blank_A4&isuseds=true
     * 
     * @param jasperFilename
     * @param isUseDs
     * @param request
     * @param response
     */
    @RequestMapping("/exportpdf")
    public void exportPdf(
            @RequestParam(name = "jasper", defaultValue = "Blank_A4_helloworld") String jasperFilename
            ,@RequestParam(name = "isuseds", defaultValue = "false") boolean isUseDs
            , HttpServletRequest request
            , HttpServletResponse response) {

        StringBuilder msgsb = new StringBuilder();
        URL jasperUrl = getClass().getResource(JASPER_DIR_PREFIX + "/" + jasperFilename + ".jasper");
        InputStream jasperInputStream = null;
        // open file
        try {
            jasperInputStream = jasperUrl.openStream();
        }catch(IOException e) {
            String msg = new FormattedMessage("open stream of jasper file '{}' failed.", jasperFilename, e).getFormattedMessage();
            msgsb.append(msg);
            log.error(msg);
        }
        
        if(jasperInputStream == null) {
            return;
        }
        
        JasperPrint jasperPrint = null;
        try {
            Map<String, Object> parameters = new HashMap<>();
//            parameters.put("title", "test Report");
            if(isUseDs) {
                jasperPrint = JasperFillManager.fillReport(jasperInputStream, parameters, springDataSource.getConnection());
            }else {
                jasperPrint = JasperFillManager.fillReport(jasperInputStream, parameters, JR_EMPTY_DATASOURCE);
            }
        } catch (JRException e) {
            String msg = new FormattedMessage("fill jasper report of '{}' failed.", jasperFilename, e).getFormattedMessage();
            msgsb.append(msg);
            log.error(msg);
        } catch (SQLException e) {
            String msg = new FormattedMessage("get connection from spring datasource for filling '{}' failed.", jasperFilename, e).getFormattedMessage();
            msgsb.append(msg);
            log.error(msg);
        }finally {
            if(jasperInputStream != null) {
                try {
                    jasperInputStream.close();
                } catch (IOException e) {
                    String msg = new FormattedMessage("Close stream of jasper file '{}' failed.", jasperFilename, e).getFormattedMessage();
                    msgsb.append(msg);
                    log.error(msg);
                }
            }
        }

        if(jasperPrint == null) {
            return;
        }

        try {
            response.setContentType("application/x-pdf");
            response.setHeader("Content-disposition", "inline; filename="+jasperFilename+".pdf");

            final OutputStream outStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
        } catch (IOException e) {
            String msg = new FormattedMessage("open output stream of response failed.", e).getFormattedMessage();
            msgsb.append(msg);
            log.error(msg);
        } catch (JRException e) {
            String msg = new FormattedMessage("export report to pdf stream of '{}' failed.", jasperFilename, e).getFormattedMessage();
            msgsb.append(msg);
            log.error(msg);
        }
    }
}
