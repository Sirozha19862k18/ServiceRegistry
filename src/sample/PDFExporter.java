package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;


public class PDFExporter {
    private final String TITLE_TEXT = "Внимание! Произошла ошибка выполнения формирования документа. Вероятнее всего протокол уже существует и имеет атрибуты 'для чтения'";
    private final Alert.AlertType ALERT = Alert.AlertType.ERROR;
    public void exportToPDF(Client client)  {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
        File file =new File(Controller.PATH_TO_PROTOCOL+"/"+client.getClientName()+"/"+client.getIncidentNumber()+".pdf");
        if(!file.exists()){
            File file2 = new File(Controller.PATH_TO_PROTOCOL+"/"+client.getClientName());
            file2.mkdir();
        }
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
        } catch (DocumentException | FileNotFoundException ex) {
         Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        document.open();
        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont(Controller.PATH_TO_RESOURCES+"/font.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException | IOException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        Font font = new Font(baseFont, 12, Font.NORMAL);
        Font titleFont = new Font(baseFont, 16, Font.BOLD );
        Font nameOfPropertiesFont = new Font(baseFont, 10, Font.NORMAL);
        Chunk serviseProtokolChunk = new Chunk("Сервисный протокол", titleFont );
        Chunk clientTextName= new Chunk("Клиент: ", nameOfPropertiesFont);
        Chunk clientNameChunk = new Chunk(client.getClientName(), titleFont);
        Chunk incidentNumberChunk = new Chunk(client.getIncidentNumber(), titleFont);
        Chunk dateIncidentText = new Chunk("Дата: ", nameOfPropertiesFont);
        Chunk dateIncidentChunk = new Chunk(client.getDateIncident(), font);
        Chunk problemNameText = new Chunk("Описание проблемы: ", nameOfPropertiesFont);
        Chunk problemNameChunk = new Chunk(client.getProblemName(), font);
        Chunk materialListTex = new Chunk("Использованные материалы: ", nameOfPropertiesFont);
        Chunk materialListChunk = new Chunk(client.getMaterialList(), font);
        Chunk fixProblemText = new Chunk ("Проведенные работы: ", nameOfPropertiesFont);
        Chunk fixProblemChunk = new Chunk(client.getFixProblem(), font);
        Chunk employerTimeText = new Chunk("Затраченное время: ", nameOfPropertiesFont);
        Chunk employerTimeChunk = new Chunk(String.valueOf(client.getEmpoyerTime()), font);
        Chunk employerCounterText = new Chunk("Количество людей решавших инцидент: ", nameOfPropertiesFont);
        Chunk employerCounterChunk = new Chunk(String.valueOf(client.getEmployerCounter()), font);
        Chunk employerNameText = new Chunk("Фамилии людей, задействованых в решении инцидента: ", nameOfPropertiesFont);
        Chunk employerNameChunk = new Chunk(client.getEmployerName(), font);
        Chunk carDrivingToIncidentText = new Chunk("Автомобиль: ", nameOfPropertiesFont);
        Chunk carDrivingToIncidentChunk = new Chunk(client.getCarDrivingToIncident(), font);
        Chunk mileageText = new Chunk("Пробег: ", nameOfPropertiesFont);
        Chunk mileageChunk = new Chunk(String.valueOf(client.getMileage())+"  км", font);
        Chunk clientSignature = new Chunk("Клиент:", font);
        Chunk serviceSignature = new Chunk("Исполнитель:", font);
        Chunk signature = new Chunk("________________________(                        )", font);
        /*Вставка логотипа*/
        Image image = null;
        try {
            image = Image.getInstance(Controller.PATH_TO_RESOURCES+"/logo.jpg");
        } catch (BadElementException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Controller.showAlertMessage("Логотип для вставки в документ не найден", e.toString(), ALERT);
        }
        /*  Формирование заголовка*/
        float[] tableHeaderWidth = {35f, 65f};
        PdfPTable tableHeader = new PdfPTable(tableHeaderWidth);
        tableHeader.setWidthPercentage(100f);
        PdfPCell cellImage = new PdfPCell(image);
        cellImage.setBorder(Rectangle.NO_BORDER);
        tableHeader.addCell(cellImage);
        PdfPTable tableSignatureHeader = new PdfPTable(1);
        PdfPCell cellServiseProtocolText = new PdfPCell(new Phrase(serviseProtokolChunk));
        cellServiseProtocolText.setFixedHeight(40f);
        cellServiseProtocolText.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_BOTTOM);
        cellServiseProtocolText.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellProtocolNumber = new PdfPCell(new Phrase(incidentNumberChunk));
        cellProtocolNumber.setBorder(Rectangle.NO_BORDER);
        tableSignatureHeader.addCell(cellServiseProtocolText);
        tableSignatureHeader.addCell(cellProtocolNumber);
        PdfPCell cellHeader = new PdfPCell(tableSignatureHeader);
        cellHeader.setBorder(Rectangle.NO_BORDER);
        tableHeader.addCell(cellHeader);
        tableHeader.completeRow();
        try {
            document.add(tableHeader);
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        /* Таблица где вписано имя клиента */
        float[] poinColumnWidthClient={30F, 120F};
        PdfPTable tableClientName = new PdfPTable(poinColumnWidthClient);
        tableClientName.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        tableClientName.setWidthPercentage(100f);
        PdfPCell cellTextClientName= new PdfPCell(new Phrase(clientTextName));
        cellTextClientName.setUseBorderPadding(true);
        cellTextClientName.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        tableClientName.addCell(cellTextClientName);
        PdfPCell cellClientName = new PdfPCell(new Phrase(clientNameChunk));
        cellClientName.setPadding(5f);
        cellClientName.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        cellClientName.setUseBorderPadding(true);
        cellClientName.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        tableClientName.addCell(cellClientName);
        tableClientName.completeRow();
        try {
            document.add(new Paragraph(" "));
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        try {
            document.add(tableClientName);
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        /* Таблица где вписана дата дефекта */
        PdfPTable tableDateIncident = new PdfPTable(poinColumnWidthClient);
        tableDateIncident.setWidthPercentage(100f);
        tableDateIncident.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        PdfPCell cellDateIncidentText = new PdfPCell(new Phrase(dateIncidentText));
        cellDateIncidentText.setUseBorderPadding(true);
        cellDateIncidentText.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        tableDateIncident.addCell(cellDateIncidentText);
        PdfPCell cellDateIncident = new PdfPCell(new Phrase(dateIncidentChunk));
        cellDateIncident.setUseBorderPadding(true);
        cellDateIncident.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        tableDateIncident.addCell(cellDateIncident);
        tableDateIncident.completeRow();
        try {
            document.add(new Paragraph(" "));
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        try {
            document.add(tableDateIncident);
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        /* Таблица где вписано наименование дефекта */
        PdfPTable tableDefect = new PdfPTable(poinColumnWidthClient);
        tableDefect.setWidthPercentage(100f);
        tableDefect.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        PdfPCell cellDefectText = new PdfPCell(new Phrase(problemNameText));
        cellDefectText.setUseBorderPadding(true);
        cellDefectText.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        tableDefect.addCell(cellDefectText);
        PdfPCell cellDefect = new PdfPCell(new Phrase(problemNameChunk));
        cellDefect.setUseBorderPadding(true);
        cellDefect.setFixedHeight(100f);
        cellDefect.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        tableDefect.addCell(cellDefect);
        tableDefect.completeRow();
        try {
            document.add(tableDefect);
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        /* Таблица со списком использованных материалов */
        PdfPTable tableMaterialList = new PdfPTable(poinColumnWidthClient);
        tableMaterialList.setWidthPercentage(100f);
        tableMaterialList.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        PdfPCell cellMaterialListText = new PdfPCell(new Phrase(materialListTex));
        cellMaterialListText.setUseBorderPadding(true);
        tableMaterialList.addCell(cellMaterialListText);
        PdfPCell cellMaterialListChunk = new PdfPCell(new Phrase(materialListChunk));
        cellMaterialListChunk.setUseBorderPadding(true);
        cellMaterialListChunk.setFixedHeight(60f);
        tableMaterialList.addCell(cellMaterialListChunk);
        tableMaterialList.completeRow();
        try {
            document.add(tableMaterialList);
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        /* Таблица со списком проведенных работ */
        PdfPTable tableFixProblem = new PdfPTable(poinColumnWidthClient);
        tableFixProblem.setWidthPercentage(100f);
        tableFixProblem.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        PdfPCell cellFixProblemText = new PdfPCell(new Phrase(fixProblemText));
        cellFixProblemText.setUseBorderPadding(true);
        tableFixProblem.addCell(cellFixProblemText);
        PdfPCell cellFixProblemChunk = new PdfPCell(new Phrase(fixProblemChunk));
        cellFixProblemChunk.setUseBorderPadding(true);
        cellFixProblemChunk.setFixedHeight(300f);
        tableFixProblem.addCell(cellFixProblemChunk);
        tableFixProblem.completeRow();
        try {
            document.add(tableFixProblem);
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        /* Таблица со списком времени и кол-ва людей */
        float [] columnManAndCount = {30F, 20F, 30F, 20F};
        PdfPTable tableManAndCount = new PdfPTable(columnManAndCount);
        tableManAndCount.setWidthPercentage(100f);
        tableManAndCount.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        PdfPCell cellEmployerTimeText = new PdfPCell(new Phrase(employerTimeText));
        cellEmployerTimeText.setUseBorderPadding(true);
        tableManAndCount.addCell(cellEmployerTimeText);
        PdfPCell cellEmployerTimeChunk = new PdfPCell(new Phrase(employerTimeChunk));
        cellEmployerTimeChunk.setUseBorderPadding(true);
        cellEmployerTimeChunk.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        tableManAndCount.addCell(cellEmployerTimeChunk);
        PdfPCell cellEmployerCounterText = new PdfPCell(new Phrase(employerCounterText));
        cellEmployerCounterText.setUseBorderPadding(true);
        tableManAndCount.addCell(cellEmployerCounterText);
        PdfPCell cellEmployerCounterChunk = new PdfPCell(new Phrase(employerCounterChunk));
        cellEmployerCounterChunk.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        cellEmployerCounterChunk.setUseBorderPadding(true);
        tableManAndCount.addCell(cellEmployerCounterChunk);
        tableManAndCount.completeRow();
        try {
            document.add(tableManAndCount);
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        /* Таблица со списком фамилий работников */
        PdfPTable tableNameEmployer = new PdfPTable(poinColumnWidthClient);
        tableNameEmployer.setWidthPercentage(100f);
        tableNameEmployer.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        PdfPCell cellEmployerNameText = new PdfPCell(new Phrase(employerNameText));
        cellEmployerNameText.setUseBorderPadding(true);
        tableNameEmployer.addCell(cellEmployerNameText);
        PdfPCell cellEmployerNameChunk = new PdfPCell(new Phrase(employerNameChunk));
        cellEmployerNameChunk.setUseBorderPadding(true);
        tableNameEmployer.addCell(cellEmployerNameChunk);
        tableNameEmployer.completeRow();
        try {
            document.add(tableNameEmployer);
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        /* Таблица с авто и пробегом */
        PdfPTable tableAutoAndMileage = new PdfPTable(columnManAndCount);
        tableAutoAndMileage.setWidthPercentage(100f);
        tableAutoAndMileage.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
        PdfPCell cellAutoText = new PdfPCell(new Phrase(carDrivingToIncidentText));
        cellAutoText.setUseBorderPadding(true);
        tableAutoAndMileage.addCell(cellAutoText);
        PdfPCell cellAutoChunk = new PdfPCell(new Phrase(carDrivingToIncidentChunk));
        cellAutoChunk.setUseBorderPadding(true);
        cellAutoChunk.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        tableAutoAndMileage.addCell(cellAutoChunk);
        PdfPCell cellMileageText = new PdfPCell(new Phrase(mileageText));
        cellMileageText.setUseBorderPadding(true);
        tableAutoAndMileage.addCell(cellMileageText);
        PdfPCell cellMileageChunk = new PdfPCell(new Phrase(mileageChunk));
        cellMileageChunk.setUseBorderPadding(true);
        cellMileageChunk.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        tableAutoAndMileage.addCell(cellMileageChunk);
        tableAutoAndMileage.completeRow();
        try {
            document.add(tableAutoAndMileage);
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        /* Формирование подписи */
        try {
            document.add(new Paragraph(" "));
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        float[] signatureWidth = {50f, 50f};
        PdfPTable tableFooterName = new PdfPTable(signatureWidth);
        tableFooterName.setWidthPercentage(100f);
        PdfPCell cellClient = new PdfPCell(new Phrase(clientSignature));
        cellClient.setBorder(Rectangle.NO_BORDER);
        tableFooterName.addCell(cellClient);
        PdfPCell cellService = new PdfPCell(new Phrase(serviceSignature));
        cellService.setBorder(Rectangle.NO_BORDER);
        cellService.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        tableFooterName.addCell(cellService);
        tableFooterName.completeRow();
        try {
            document.add(tableFooterName);
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);
        }
        try {
            document.add(new Paragraph(" "));
        } catch (DocumentException ex) {
            Controller.showAlertMessage(TITLE_TEXT, ex.toString(), ALERT);

        }
        PdfPTable tableSignature = new PdfPTable(signatureWidth);
        tableSignature.setWidthPercentage(100f);
        PdfPCell signature1 = new PdfPCell(new Phrase(signature));
        signature1.setBorder(Rectangle.NO_BORDER);
        tableSignature.addCell(signature1);
        PdfPCell signature2 = new PdfPCell(new Phrase(signature));
        signature2.setBorder(Rectangle.NO_BORDER);
        signature2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
        tableSignature.addCell(signature2);
        tableSignature.completeRow();
        try {
            document.add(tableSignature);
        } catch (DocumentException ex) {
            String headerText = "Документ не создан";
            String contentText = ex.toString();
            Alert.AlertType alertType= Alert.AlertType.ERROR;
            Controller.showAlertMessage(headerText, contentText, alertType);
        }
                document.close();
        Controller.showAlertMessage("Протокол успешно экспотрирован", "Местоположение: "+ file.toString(), Alert.AlertType.INFORMATION);
    }
}
