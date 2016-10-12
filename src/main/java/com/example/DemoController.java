package com.example;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ritesh on 04-10-2016.
 */
@RestController
@IntegrationComponentScan
public class DemoController {

    @Autowired
    private DemoService demoService;


    @Autowired
    private RemoteFileTemplate<FTPFile> remoteFileTemplate;



    @RequestMapping("/")
    public boolean hello() throws InterruptedException {
        System.out.println("Hello, World....");

        boolean b;// = remoteFileTemplate.exists("/temp.pdf");

        b = demoService.sendToFtp("C:/temp.zip");

        return b;//demoService.sendMsg();

    }

}
