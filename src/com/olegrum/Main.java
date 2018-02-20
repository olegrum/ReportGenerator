package com.olegrum;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        Settings settings = null;
        if (args.length != 3) throw new Exception("Wrong number of arguments. Try 'settings' 'source' 'report'");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            settings = (Settings) jaxbUnmarshaller.unmarshal(new File(args[0]));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        ReportGenerator generator = new ReportGenerator(settings, new SourceData(new File(args[1]), settings), new File(args[2]));
        generator.write();
    }
}
