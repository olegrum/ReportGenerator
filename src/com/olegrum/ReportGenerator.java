package com.olegrum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ReportGenerator {
    private Settings settings;
    private File report;
    private SourceData source;
    private char delimiter = '|';
    private String pageEnder;
    private char separator = '-';

    public ReportGenerator(Settings settings, SourceData source, File report) throws Exception {
        this.settings = settings;
        checkPageWidth();
        pageEnder = String.format("%-" + settings.getPage().getWidth() + "s", '~') + System.lineSeparator();
        this.source = source;
        this.report = report;
    }

    private byte[] constructPageTop() throws UnsupportedEncodingException {
        StringBuilder top = new StringBuilder("|");
        settings.getColumns().forEach(column ->
                top.append(' ').append(String.format("%-" + column.getWidth() + "s", column.getTitle())).append(' ').append(delimiter));
        top.append(System.lineSeparator());
        return top.toString().getBytes("UTF-16LE");
    }

    private byte[] constructSeparation() throws UnsupportedEncodingException {
        return (String.format("%" + settings.getPage().getWidth() + "s", " ").replace(' ', separator) + System.lineSeparator()).getBytes("UTF-16LE");
    }

    private void checkPageWidth() throws Exception {
        int columnsWidth = settings.getColumns().stream().mapToInt(column::getWidth).sum() + settings.getColumns().size() * 3 + 1;
        if (columnsWidth > settings.getPage().getWidth())
            throw new Exception("Can't fit all columns in set page width. Set width: " + settings.getPage().getWidth() + " Actual width: " + columnsWidth);
    }

    public void write() {
        try (FileOutputStream fos = new FileOutputStream(report, false)) {
            ArrayList<String> next;
            fos.write(constructPageTop());
            fos.write(constructSeparation());
            int count = 2;
            while ((next = source.getNext()) != null) {
                if (next.size() > settings.getPage().getHeight()) System.out.println("you wot m8");
                if ((count + next.size() + 1) > settings.getPage().getHeight()) {
                    fos.write(pageEnder.getBytes("UTF-16LE"));
                    fos.write(constructPageTop());
                    fos.write(constructSeparation());
                    count = 2;
                }
                for (String string : next) {
                    fos.write(string.getBytes("UTF-16LE"));
                    count++;
                }
                fos.write(constructSeparation());
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
