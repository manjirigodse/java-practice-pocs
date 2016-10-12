package com.example;

import com.example.Config;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by Ritesh on 10-10-2016.
 */
@Service
public class DemoService {


    @Autowired
    private RemoteFileTemplate<FTPFile> remoteFileTemplate;

    @Autowired
    Config.MyGateway gateway;

    DemoService(){

    }
    public  boolean  sendToFtp(String filePath) throws InterruptedException {


        File f = new File(filePath);
        String fileName = "/" + f.getName();

        boolean exist = remoteFileTemplate.exists(fileName);
        if(!exist) {
            gateway.sendToFtp(new File(filePath));
            Thread.sleep(1000);
            return remoteFileTemplate.exists(fileName);
        }

        return !exist;
    }
}
