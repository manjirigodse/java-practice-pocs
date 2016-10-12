package com.example
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
/**
 * Created by Ritesh on 10-10-2016.
 */
@ContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
class DemoServiceTest extends Specification {

    @Autowired
    DemoService demoService

    def "SendToFtp"() {
        expect:
                demoService.sendToFtp(filePath) == status
        where:
                filePath					| status
                "c:/temp.zip"				| true
                "c:/temp.pdf"				| true
                "c:/temp.jpg"               | true
                "c:/temp.xml"               | true
                "c:/temp.txt"               | false

    }
}
