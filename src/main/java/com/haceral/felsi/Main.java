package com.haceral.felsi;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.write.metadata.WriteSheet;
import cn.idev.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<List<String>> head = List.of(List.of("Demo","A"), List.of("Demo","B"), List.of("Demo","C"), List.of("Demo","D"));
        List<List<Object>> contentData = new ArrayList<>(11);
        for (int i = 1; i <= 10; i++) {
            contentData.add(List.of(i, LocalDate.now(), new Date(), "str" + i));
        }

        // 丢失样式
        String[] ptr = null;
        // 设置为非 null（如 "B", "C"），样式可以保留（通过手动调用setOriginCellStyle实现）
        String[] keep = new String[] { "B", "C" };
        try (ExcelWriter excelWriter = FastExcel.write("demo.xlsx").head(head).useDefaultStyle(false)
            .registerWriteHandler(new CustomCellStyleStrategy(ptr))
            .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
            .build()) {
            WriteSheet sheet = new WriteSheet();
            sheet.setSheetNo(0);
            sheet.setSheetName("Demo Sheet");

            excelWriter.write(contentData, sheet);
            excelWriter.finish();
        }
    }
}
