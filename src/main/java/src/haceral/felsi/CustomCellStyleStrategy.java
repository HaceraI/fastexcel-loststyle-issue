package src.haceral.felsi;

import cn.idev.excel.metadata.Head;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.write.handler.CellWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteTableHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义单元格样式.
 *
 * @author Haceral
 * @version V1.0
 */
public class CustomCellStyleStrategy implements CellWriteHandler {
    private final String[] dateColumns;
    private final Map<String, XSSFCellStyle> styleCache = new ConcurrentHashMap<>();

    public CustomCellStyleStrategy() { this.dateColumns = null; }

    public CustomCellStyleStrategy(String[] dateColumns) { this.dateColumns = dateColumns; }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        Workbook workbook = writeSheetHolder.getParentWriteWorkbookHolder().getWorkbook();
        int rowIndex = cell.getRowIndex();

        XSSFCellStyle cellStyle = null;
        if (isHead != null && isHead) {
            cellStyle = getStyle(workbook, "HEADER");
        } else {
            cellStyle = getStyle(workbook, "CONTENT_COL_" + cell.getColumnIndex());
        }

        cell.setCellStyle(cellStyle);

        if (dateColumns != null && Arrays.stream(dateColumns).anyMatch(head.getHeadNameList()::contains)) {
            cellDataList.getFirst().setOriginCellStyle(cellStyle);
        }
    }

    private XSSFCellStyle getStyle(Workbook workbook, String key) {
        return styleCache.computeIfAbsent(key, k -> createStyle(workbook, k));
    }

    private XSSFCellStyle createStyle(Workbook workbook, String styleType) {
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName("宋体");

        if ("HEADER".equals(styleType)) {
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
            font.setColor(new XSSFColor(java.awt.Color.WHITE, null));
            style.setFillForegroundColor(new XSSFColor(java.awt.Color.decode("#000"), null));
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorder(style, "#D9D9D9", BorderStyle.THIN);
        } else if (styleType.startsWith("CONTENT_COL_")) {
            font.setFontHeightInPoints((short) 10);
            style.setAlignment(HorizontalAlignment.LEFT);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorder(style, "#E9E9E9", BorderStyle.THIN);
        }

        style.setFont(font);
        return style;
    }

    private void setBorder(XSSFCellStyle style, String hexColor, BorderStyle borderStyle) {
        XSSFColor color = new XSSFColor(java.awt.Color.decode(hexColor), null);
        style.setBorderTop(borderStyle);
        style.setTopBorderColor(color);
        style.setBorderRight(borderStyle);
        style.setRightBorderColor(color);
        style.setBorderBottom(borderStyle);
        style.setBottomBorderColor(color);
        style.setBorderLeft(borderStyle);
        style.setLeftBorderColor(color);
    }
}
