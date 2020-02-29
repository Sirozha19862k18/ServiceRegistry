package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;




public class Controller {

    ObservableList<String> observableListClient;
    ObservableList<String> observableListIncident;
    Document doc;
    NodeList nodeList;
    int section;
    Node childNodeOfClient;
    NodeList childNode;
    int countMouseClick = 0;
    Element protocolServiseProperties;
    Client client;
    Stage addClientWindow;
    String clienttoDeleteChange;
    int clientCounter;


    @FXML    ListView<String> list;
    @FXML    TextField numberOfIncident;
    @FXML    TextField dateOfIncidentTextField;
    @FXML    TextArea problemNameTextArea;
    @FXML    TextField employerNameTextField;
    @FXML    TextField carDrivingToIncidentTextField;
    @FXML    TextField employerCounterTextField;
    @FXML    TextField mileageTextField;
    @FXML    TextArea fixProblemTextArea;
    @FXML    TextField employerTimeTextField;
    @FXML    Button SaveXML;
    @FXML    Button addClientWindowCloseBtn;
    @FXML    TextField newClientNameTextField;
    @FXML    Button addClientToBaseBtn;
    @FXML    ListView<String> clientListToDeleteList;
    @FXML    Button deleteSelectedCliendBtn;
    @FXML    TextArea materialListTextArea;
    @FXML    Label clientCounterLabel;


    @FXML
    public void appExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void loadXMLfile() throws ParserConfigurationException, IOException, SAXException {
        File xmlFille = new File("src/sample/service.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbuilder = factory.newDocumentBuilder();
        doc = dbuilder.parse(xmlFille);
        observableListClient = FXCollections.observableArrayList();
        observableListIncident = FXCollections.observableArrayList();
    }

    @FXML
    public void openXML(ActionEvent actionEvent) throws IOException, SAXException, ParserConfigurationException {
        client = new Client();
        section = 1;
        loadXMLfile();
        getClientList();
        list.setItems(observableListClient);
    }

    /*Сохранить изменения в протоколе сервиса*/
    @FXML
    public void saveXML(ActionEvent actionEvent) {
        String argument = "save";
        incidentDetails(argument);
    }

    /* Вывод списка клиентов */
    void getClientList() {
        clientCounter=0;
        nodeList = doc.getElementsByTagName("client");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                observableListClient.add(eElement.getAttribute("client"));
                clientCounter++;
            }
        }
    }

    /* Вывод списка инцидентов */
    void getIncidentList() {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element eElement = (Element) nodeList.item(i);
            if (eElement.getAttribute("client").equals(client.getClientName())) {
                if (nodeList.item(i).hasChildNodes()) {
                    childNode = nodeList.item(i).getChildNodes();
                    for (int indx = 0; indx < childNode.getLength(); indx++) {
                        childNodeOfClient = childNode.item(indx);
                        if (childNodeOfClient.getNodeType() == Node.ELEMENT_NODE) {
                            Element elementChildOfClient = (Element) childNodeOfClient;
                            observableListIncident.add(elementChildOfClient.getAttribute("incidentNumber"));
                        }
                    }
                }
            }
        }
    }

    void SaveIncident() {
        if (protocolServiseProperties.hasAttribute("dateIncident")) {
            protocolServiseProperties.setAttribute("dateIncident", dateOfIncidentTextField.getText());
        }
        if (protocolServiseProperties.hasAttribute("problemName")) {
            protocolServiseProperties.setAttribute("problemName", problemNameTextArea.getText());
        }
        if (protocolServiseProperties.hasAttribute("employerCounter")) {
            protocolServiseProperties.setAttribute("employerCounter", employerCounterTextField.getText());
        }
        if (protocolServiseProperties.hasAttribute("employerName")) {
            protocolServiseProperties.setAttribute("employerName", employerNameTextField.getText());
        }
        if (protocolServiseProperties.hasAttribute("carDrivingToIncident")) {
            protocolServiseProperties.setAttribute("carDrivingToIncident", carDrivingToIncidentTextField.getText());
        }
        if (protocolServiseProperties.hasAttribute("mileage")) {
            protocolServiseProperties.setAttribute("mileage", mileageTextField.getText());
        }
        if (protocolServiseProperties.hasAttribute("fixProblem")) {
            protocolServiseProperties.setAttribute("fixProblem", fixProblemTextArea.getText());
        }
        if (protocolServiseProperties.hasAttribute("employerTime")) {
            protocolServiseProperties.setAttribute("employerTime", employerTimeTextField.getText());
        }
        if (protocolServiseProperties.hasAttribute("materialList")) {
            protocolServiseProperties.setAttribute("materialList", materialListTextArea.getText());
        }

    }

    /* Вывод подробностей инцидента*/
    void incidentDetails(String argument) {
        for (int i = 0; i < childNode.getLength(); i++) {
            childNodeOfClient = childNode.item(i);
            if (childNodeOfClient.getNodeType() == Node.ELEMENT_NODE) {
                Element elementChildOfClient = (Element) childNodeOfClient;
                if (elementChildOfClient.getAttribute("incidentNumber").equals(client.getIncidentNumber())) {
                    if (elementChildOfClient.hasChildNodes()) {
                        NodeList propertiesNode = elementChildOfClient.getChildNodes();
                        for (i = 0; i < propertiesNode.getLength(); i++) {
                            Node propNode = propertiesNode.item(i);
                            if (propNode.getNodeType() == Node.ELEMENT_NODE)
                                if (argument.equals("print")) {
                                    {
                                        protocolServiseProperties = (Element) propNode;
                                        incidentPrint(elementChildOfClient);
                                    }
                                } else if (argument.equals("save")) {
                                    {
                                        protocolServiseProperties = (Element) propNode;
                                        SaveIncident();
                                        writeDocument(doc);
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

    public void incidentPrint(Element elementChildOfClient) {
        numberOfIncident.setEditable(false);
        numberOfIncident.setDisable(true);
        numberOfIncident.setText(elementChildOfClient.getAttribute("incidentNumber"));
        if (protocolServiseProperties.hasAttribute("dateIncident")) {
            String dateIncident = protocolServiseProperties.getAttribute("dateIncident");
            client.setDateIncident(dateIncident);
            dateOfIncidentTextField.setText(dateIncident);
        }
        if (protocolServiseProperties.hasAttribute("problemName")) {
            String problemName = protocolServiseProperties.getAttribute("problemName");
            client.setProblemName(problemName);
            problemNameTextArea.setText(problemName);
        }
        if (protocolServiseProperties.hasAttribute("employerCounter")) {
            String employerCounter = protocolServiseProperties.getAttribute("employerCounter");
            client.setEmployerCounter(Integer.parseInt(employerCounter));
            employerCounterTextField.setText(employerCounter);
        }
        if (protocolServiseProperties.hasAttribute("employerName")) {
            String employerName = protocolServiseProperties.getAttribute("employerName");
            client.setEmployerName(employerName);
            employerNameTextField.setText(employerName);
        }
        if (protocolServiseProperties.hasAttribute("carDrivingToIncident")) {
            String carDrivingToIncident = protocolServiseProperties.getAttribute("carDrivingToIncident");
            client.setCarDrivingToIncident(carDrivingToIncident);
            carDrivingToIncidentTextField.setText(carDrivingToIncident);
        }
        if (protocolServiseProperties.hasAttribute("mileage")) {
            String mileage = protocolServiseProperties.getAttribute("mileage");
            client.setMileage(Integer.parseInt(mileage));
            mileageTextField.setText(mileage);
        }
        if (protocolServiseProperties.hasAttribute("fixProblem")) {
            String fixProblem = protocolServiseProperties.getAttribute("fixProblem");
            client.setFixProblem(fixProblem);
            fixProblemTextArea.setText(fixProblem);
        }
        if (protocolServiseProperties.hasAttribute("employerTime")) {
            String employerTime = protocolServiseProperties.getAttribute("employerTime");
            client.setEmpoyerTime(Integer.parseInt(employerTime));
            employerTimeTextField.setText(employerTime);
        }
        if (protocolServiseProperties.hasAttribute("materialList")) {
            String materialList = protocolServiseProperties.getAttribute("materialList");
            client.setMaterialList(materialList);
            materialListTextArea.setText(materialList);
        }
    }

    public void listMouseClicked(MouseEvent mouseEvent) {

        countMouseClick++;
        if (countMouseClick == 2) {
            if (section == 1) {   //обработка страниц вывода, чтобы по двойному таму на чилдовой ноде не выводился снова список
                countMouseClick = 0;
                String clientChange = list.getSelectionModel().getSelectedItem();
                observableListClient.clear();
                client.setClientName(clientChange);
                getIncidentList();
                list.setItems(observableListIncident);
                section = 2;

            }
            if (section == 2) {
                countMouseClick = 0;
                String incidentChange = list.getSelectionModel().getSelectedItem();
                client.setIncidentNumber(incidentChange);
                String argument = "print";
                incidentDetails(argument);
                SaveXML.setDisable(false);

            } else {
                countMouseClick = 0;
            }
        }
    }

    private void writeDocument(Document document) {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream("service.xml");
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
            fos.close();

        } catch (TransformerException | IOException e) {
            e.printStackTrace(System.out);
        }
    }


    @FXML
    public void addClientWindowClose(ActionEvent actionEvent) {
        addClientWindow = (Stage) addClientWindowCloseBtn.getScene().getWindow();
        addClientWindow.close();
    }

    @FXML
    public void addClientToBase(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException {
        String newClientName = newClientNameTextField.getText();
        loadXMLfile();
        Node root = doc.getDocumentElement();
        Element book = doc.createElement("client");
        book.setAttribute("client", newClientName);
        root.appendChild(book);
        writeDocument(doc);
    }

    public void getClientListToDelete(ActionEvent actionEvent) throws IOException, SAXException, ParserConfigurationException {
        loadXMLfile();
        getClientList();
        clientListToDeleteList.setItems(observableListClient);
    }
/*Процедура удаления клиента из базы*/
    public void deleteSelectedClient(ActionEvent actionEvent) {

        nodeList = doc.getElementsByTagName("client");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                if (eElement.getAttribute("client").equals(clienttoDeleteChange)) {
                    eElement.getParentNode().removeChild(eElement);
                }
            }
           writeDocument(doc);
            deleteSelectedCliendBtn.setDisable(true);
        }
    }
  /*  Выбор клиента для последующего удаления из базы */
    public void selectClientToDeleteList(MouseEvent mouseEvent) {
        clienttoDeleteChange = clientListToDeleteList.getSelectionModel().getSelectedItem();
        deleteSelectedCliendBtn.setDisable(false);
            }

           /* Вывод количества клиентов в базе*/
    public void getClientCounterStatistics(ActionEvent actionEvent) {
clientCounterLabel.setText(String.valueOf(clientCounter));
    }

    public void exportToPDF(ActionEvent actionEvent) throws IOException, DocumentException {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(client.getIncidentNumber()+".pdf"));

        document.open();
        BaseFont baseFont = BaseFont.createFont("src/sample/font.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
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
        Image image = Image.getInstance("src/sample/resource/logo.jpg");
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
        document.add(tableHeader);
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
        document.add(new Paragraph(" "));
        document.add(tableClientName);
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
        document.add(new Paragraph(" "));
        document.add(tableDateIncident);
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
        document.add(tableDefect);
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
        document.add(tableMaterialList);
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
        document.add(tableFixProblem);
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
        document.add(tableManAndCount);
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
        document.add(tableNameEmployer);
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
        document.add(tableAutoAndMileage);
        /* Формирование подписи */
        document.add(new Paragraph(" "));
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
        document.add(tableFooterName);
        document.add(new Paragraph(" "));
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
        document.add(tableSignature);


        document.close();
    }
}

