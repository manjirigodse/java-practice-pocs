package com.example;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.ftp.Ftp;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.PollableChannel;

import java.io.File;

/**
 * Created by Ritesh on 10-10-2016.
 */
@Configuration

public class Config {


    private String hostAddress="localhost";

    private Integer portNo=29;

    private String userId="User1";

    private String password="user";

    @Bean
    public String customBean(){
        return new String("From custom bean");
    }


    @Bean
    public SessionFactory<FTPFile> ftpSessionFactory() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost(hostAddress);
        sf.setPort(portNo);
        sf.setUsername(userId);
        sf.setPassword(password);
        return new CachingSessionFactory<FTPFile>(sf);
    }


    @Bean(name = "toFtpChannel")
    public PollableChannel toFtpChannel() {
        return new QueueChannel(10);
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedDelay(1000).get();
    }

    @Bean
    public IntegrationFlow ftpOutboundFlow() {
        return IntegrationFlows.from("toFtpChannel")
                .handle(Ftp.outboundAdapter(ftpSessionFactory(), FileExistsMode.FAIL)
                                .useTemporaryFileName(false)
                                .fileNameExpression("headers['" + FileHeaders.FILENAME + "']")
                                .remoteDirectory("/")
                ).get();
    }

    @MessagingGateway
    public interface MyGateway {
        @Gateway(requestChannel = "toFtpChannel", replyChannel = "toFtpChannel",replyTimeout = 1000)
        void sendToFtp(File file);
    }

       @Bean
    public RemoteFileTemplate<FTPFile> remoteFileTemplateBean(){
        return  new RemoteFileTemplate<>(ftpSessionFactory());
    }

}
