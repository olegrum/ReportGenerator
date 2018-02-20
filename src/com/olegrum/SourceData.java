package com.olegrum;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SourceData {
    private ArrayList<ArrayList<String>> data = new ArrayList<>();
    private Settings settings;

    public SourceData(File data_file, Settings settings) throws Exception {
        this.settings = settings;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(data_file), "UTF-16LE"))) {
            String temp;
            while ((temp = reader.readLine()) != null) {
                if (temp.startsWith("\uFEFF")) temp = temp.substring(1);
                data.add(new ArrayList<>(Arrays.asList(temp.split("\t"))));
                if (data.get(data.size() - 1).size() != settings.getColumns().size())
                    throw new Exception("Number of columns in data source does not match number in settings.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getNext() {
        if (data.isEmpty()) return null;
        ArrayList<String> next = new ArrayList<>();
        ArrayList<String> current_array = data.get(0);
        while (current_array.stream().anyMatch(s -> !s.isEmpty())) {
            StringBuilder current_string = new StringBuilder("|");
            for (int i = 0; i < current_array.size(); ++i) {
                current_string.append(' ');
                if (current_array.get(i).length() <= settings.getColumns().get(i).getWidth()) {
                    current_string.append(String.format("%-" + settings.getColumns().get(i).getWidth() + "s", current_array.get(i))).append(" |");
                    current_array.set(i, "");
                } else {
                    String[] split_string = current_array.get(i).split("(?<=[^\\p{L}0-9'])");

                    if (split_string[0].length() <= settings.getColumns().get(i).getWidth()) {
                        StringBuilder temp_string = new StringBuilder(split_string[0]);
                        int current_tail = 0;
                        for (int k = 1; k < split_string.length; ++k) {
                            if ((temp_string.length() + split_string[k].length()) <= settings.getColumns().get(i).getWidth() && (k - current_tail) == 1) {
                                temp_string.append(split_string[k]);
                                current_tail++;
                            }
                        }
                        current_array.set(i, current_array.get(i).replaceFirst(temp_string.toString(), ""));
                        temp_string = new StringBuilder(String.format("%-" + settings.getColumns().get(i).getWidth() + "s", temp_string.toString()));
                        temp_string.append(" |");
                        current_string.append(temp_string);
                    } else {
                        current_string.append(String.format("%-" + settings.getColumns().get(i).getWidth() + "s", current_array.get(i).substring(0, settings.getColumns().get(i).getWidth()))).append(" |");
                        current_array.set(i, current_array.get(i).replace(current_array.get(i).substring(0, settings.getColumns().get(i).getWidth()), ""));
                    }
                }
            }
            current_string.append(System.lineSeparator());
            next.add(current_string.toString());
        }
        data.remove(0);
        return next;
    }
}
