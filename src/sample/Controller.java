package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;



public class Controller {

    public final String PATH_TO_DATABASE= "database/service.xml";
    public static final String PATH_TO_PROTOCOL = "Протоколы";
    public static final String  PATH_TO_RESOURCES = "resource";

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
    String clienttoDeleteChange;
    int clientCounter;
    boolean textFieldSetEditable=false; //Разрешает редактировать поля протокола сервиса


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
    @FXML    TextField newClientNameTextField;
    @FXML    Button addClientToBaseBtn;
    @FXML    ListView<String> clientListToDeleteList;
    @FXML    Button deleteSelectedCliendBtn;
    @FXML    TextArea materialListTextArea;
    @FXML    TextField newIncidentNumberTextField;
    @FXML    Button confirmNewIncidentNumberBtn;
    @FXML    Label selectedClientLabel;
    @FXML    Button newIncidentNumberBtn;
    @FXML    Button deleteSelectedProtocolBtn;
    @FXML    Button checkProtokolBtn;
    @FXML    Label clientToProtocolDeleteLabel;
    @FXML    Label protocolToProtocolDeleteLabel;
    @FXML    Button exportToPDFBtn;


    public void loadXMLfile()  {

        File xmlFille = new File(PATH_TO_DATABASE);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder dbuilder = null;
        try {
            dbuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            showAlertMessage("Ошибка обработки файла данных", e.toString(), Alert.AlertType.ERROR);
        }
        try {
            doc = dbuilder.parse(xmlFille);
        } catch (SAXException e) {
            showAlertMessage("Ошибка парсинга файла данных", e.toString(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            showAlertMessage("Ошибка чтения файла данных", e.toString(), Alert.AlertType.ERROR);
        }
        observableListClient = FXCollections.observableArrayList();
        observableListIncident = FXCollections.observableArrayList();
    }

 /*   Загрузка клиентов в ClientList */
    public void showClientList(ActionEvent actionEvent)  {
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
        exportToPDFBtn.setDisable(true);
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
        exportToPDFBtn.setDisable(false);
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
               checkProtokolBtn.setDisable(false);
                clientToProtocolDeleteLabel.setText(client.getClientName());
                protocolToProtocolDeleteLabel.setText(client.getIncidentNumber());

            } else {
                countMouseClick = 0;
            }
        }
    }

   /* Запись сформированного xml в файл*/
    private void writeDocument(Document document) {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream(PATH_TO_DATABASE);
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
            fos.close();

        } catch (TransformerException | IOException e) {
            e.printStackTrace(System.out);
        }
    }

    public void addClientToBase(ActionEvent actionEvent)  {
        String newClientName = newClientNameTextField.getText();
        loadXMLfile();
        Node root = doc.getDocumentElement();
        Element newClient = doc.createElement("client");
        newClient.setAttribute("client", newClientName);
        root.appendChild(newClient);
        writeDocument(doc);
        showAlertMessage("Новый клиент добавлен в базу", "Клиент "+ newClientName+ " успешно добавлен", Alert.AlertType.INFORMATION);
    }

    public void getClientListToDelete(ActionEvent actionEvent)  {
        loadXMLfile();
        getClientList();
        clientListToDeleteList.setItems(observableListClient);
    }
/*Процедура удаления клиента из базы*/
    public void deleteSelectedClient(ActionEvent actionEvent) {
       String titleConfirmText ="Внимание";
        String contentConfirmText = "Вы точно хотите удалить из базы клиента " + clienttoDeleteChange;
        boolean confirUserChange = showConfirmationDialog(titleConfirmText, contentConfirmText);
        System.out.println(confirUserChange);
        if (confirUserChange==true) {
            nodeList = doc.getElementsByTagName("client");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    if (eElement.getAttribute("client").equals(clienttoDeleteChange)) {
                        eElement.getParentNode().removeChild(eElement);
                    }
                }
            }
            deleteSpacingInDocument();
            writeDocument(doc);
            deleteSelectedCliendBtn.setDisable(true);
            String headerText = "Успешно!";
            String contentText = "Клиент " + clienttoDeleteChange + " успешно удален из базы";
            Alert.AlertType alertType = Alert.AlertType.INFORMATION;
            showAlertMessage(headerText, contentText, alertType);
        }
    }
  /*  Выбор клиента для последующего удаления из базы */
    public void selectClientToDeleteList(MouseEvent mouseEvent) {
        clienttoDeleteChange = clientListToDeleteList.getSelectionModel().getSelectedItem();
        deleteSelectedCliendBtn.setDisable(false);
            }



    public void exportToPDF(ActionEvent actionEvent)  {
    PDFExporter exportFileToPDF = new PDFExporter();
    exportFileToPDF.exportToPDF(client);
    }

    /*Новый протокол сервиса создание*/
    public void newIncidentNumberBtnPressed(ActionEvent actionEvent) {

        if(client!=null&&client.getClientName()!=null){
            confirmNewIncidentNumberBtn.setVisible(true);
            newIncidentNumberTextField.setVisible(true);
            selectedClientLabel.setText(client.getClientName());
            newIncidentNumberBtn.setDisable(true);
        }
        else {
            String headerText = "Не выбран клиент для которого нужно создать новый протокол сервиса";
            String contentText = "Для выбора клиента необходимо нажать кнопку <Вывести список клиентов>, а затем выбрать нужного вам клиента";
             Alert.AlertType alertType= Alert.AlertType.WARNING;
            showAlertMessage(headerText, contentText, alertType);
        }

    }


     void  formSetEditable(boolean textFieldSetEditable){
        textFieldSetEditable =this.textFieldSetEditable;
        numberOfIncident.setEditable(textFieldSetEditable);
        dateOfIncidentTextField.setEditable(textFieldSetEditable);
        employerCounterTextField.setEditable(textFieldSetEditable);
        problemNameTextArea.setEditable(textFieldSetEditable);
        employerNameTextField.setEditable(textFieldSetEditable);
        carDrivingToIncidentTextField.setEditable(textFieldSetEditable);
        mileageTextField.setEditable(textFieldSetEditable);
        fixProblemTextArea.setEditable(textFieldSetEditable);
        employerTimeTextField.setEditable(textFieldSetEditable);
        materialListTextArea.setEditable(textFieldSetEditable);
    }
    /* Добавление нового протокола сервиса */
    public void confirmNewIncidentNumberBtn(ActionEvent actionEvent) {
        newIncidentNumberBtn.setDisable(false);
        String newIncidentNumber=newIncidentNumberTextField.getText();
        client.setIncidentNumber(newIncidentNumber);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element eElement = (Element) nodeList.item(i);
            if (eElement.getAttribute("client").equals(client.getClientName())) {
                Node clientNode = nodeList.item(i);
                Element newIncidentNumberNode = doc.createElement("incidentNumber");
                clientNode.appendChild(newIncidentNumberNode);
                newIncidentNumberNode.setAttribute("incidentNumber", newIncidentNumber);
                newIncidentNumberNode.appendChild(doc.createElement("problemName"));
                newIncidentNumberNode.appendChild(doc.createElement("materialList"));
                newIncidentNumberNode.appendChild(doc.createElement("employerTime"));
                newIncidentNumberNode.appendChild(doc.createElement("employerCounter"));
                newIncidentNumberNode.appendChild(doc.createElement("employerName"));
                newIncidentNumberNode.appendChild(doc.createElement("carDrivingToIncident"));
                newIncidentNumberNode.appendChild(doc.createElement("mileage"));
                newIncidentNumberNode.appendChild(doc.createElement("fixProblem"));
                for (i=0; i<newIncidentNumberNode.getChildNodes().getLength(); i++){
                    Element incidentNumberChildNode= (Element) newIncidentNumberNode.getChildNodes().item(i);
                    switch (i) {
                        case 0:
                            incidentNumberChildNode.setAttribute("problemName", "");
                            break;
                        case 1:
                            incidentNumberChildNode.setAttribute("materialList", "");
                            break;
                        case 2:
                            incidentNumberChildNode.setAttribute("employerTime", "0");
                            break;
                        case 3:
                            incidentNumberChildNode.setAttribute("employerCounter", "0");
                            break;
                        case 4:
                            incidentNumberChildNode.setAttribute("employerName", "");
                            break;
                        case 5:
                            incidentNumberChildNode.setAttribute("carDrivingToIncident", "");
                            break;
                        case 6:
                            incidentNumberChildNode.setAttribute("mileage", "0");
                            break;
                        case 7:
                            incidentNumberChildNode.setAttribute("fixProblem", "");
                            break;
                    }
                }

                writeDocument(doc);
                String headerText = "Данные успешно внесены";
                String contentText = "Для клиента "+ client.getClientName()+" протокол сервиса № "+ client.getIncidentNumber()+ " успешно создан";
                Alert.AlertType alertType= Alert.AlertType.INFORMATION;
                showAlertMessage(headerText, contentText, alertType);
                confirmNewIncidentNumberBtn.setVisible(false);
                newIncidentNumberTextField.setVisible(false);

            }
        }
    }
/*Удаление выбранного протокола сервиса*/
    public void deleteSelectedProtocol(ActionEvent actionEvent)  {
        if(client!=null&&client.getClientName()!=null&&client.getIncidentNumber()!=null) {
            System.out.println(1);
            nodeList = doc.getElementsByTagName("client");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    if (eElement.getAttribute("client").equals(client.getClientName())) {
                        if (node.hasChildNodes()) {
                            NodeList protocolChildNodeList = node.getChildNodes();
                            for (int j = 0; j < protocolChildNodeList.getLength(); j++) {
                                Node protocolChildNode = protocolChildNodeList.item(j);
                                if (protocolChildNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element protocolElement = (Element) protocolChildNode;
                                    if (protocolElement.getAttribute("incidentNumber").equals(client.getIncidentNumber())) {
                                        protocolElement.getParentNode().removeChild(protocolElement);
                                    }
                                }
                            }

                        }
                    }
                }
            }

            deleteSpacingInDocument();
            writeDocument(doc);
            String headerText = "Протокол успешно удален";
            String contentText = "Протокол № "+ client.getIncidentNumber()+" у клиента "+ client.getClientName()+ " успешно удален";
            Alert.AlertType alertType= Alert.AlertType.INFORMATION;
            showAlertMessage(headerText, contentText, alertType);
        }
        else{
            String headerText = "Не выбран протокол для удаления";
            String contentText = "Для выбора клиента необходимо нажать кнопку <Вывести список клиентов>, выбрать нужного вам клиента, а затем выбрать протокол для удаления";
            Alert.AlertType alertType= Alert.AlertType.WARNING;
            showAlertMessage(headerText, contentText, alertType);
        }
    }


   /* Удаления пустых строк в XML файле. Иначе крашится вывод клиентов*/
    void deleteSpacingInDocument()  {
        XPath xp = XPathFactory.newInstance().newXPath();
        NodeList nl = null;
        try {
            nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            showAlertMessage("Ошибка оптимизации файла данных после удаления. Возможно файл был открыт в другой программе", e.toString(), Alert.AlertType.ERROR);
        }
        for (int i=0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            node.getParentNode().removeChild(node);
        }
    }

    static void showAlertMessage(String headerText, String contentText, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.initOwner(Main.getPrimaryStage());
        alert.setTitle("Внимание");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

/*    Вывод диалога подтверждения */
   static boolean showConfirmationDialog (String titleText,String  contentText) {
        boolean confirmUserChange = false;
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setTitle(titleText);
       alert.setHeaderText("Подтвердите действие");
       alert.setContentText(contentText);
       ButtonType okButton = new ButtonType("Да", ButtonBar.ButtonData.YES);
       ButtonType noButton = new ButtonType("Нет", ButtonBar.ButtonData.NO);
       alert.getButtonTypes().setAll(okButton, noButton);
       Optional<ButtonType> result = alert.showAndWait();
      if(result.get().getButtonData()==ButtonBar.ButtonData.YES){
      confirmUserChange= true;
      }

     return confirmUserChange;
    }

    public void CheckProtokol(ActionEvent actionEvent)  {
      /* Проверяем правильность даты инцидента*/
       String date = dateOfIncidentTextField.getText();
       dateOfIncidentTextField.setText(OutputCheckerUtils.format(date));
       SaveXML.setDisable(false);
    }
}


