package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.omg.CORBA.Environment;
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
    @FXML    Button addClientWindowCloseBtn;
    @FXML    TextField newClientNameTextField;
    @FXML    Button addClientToBaseBtn;
    @FXML    ListView<String> clientListToDeleteList;
    @FXML    Button deleteSelectedCliendBtn;
    @FXML    TextArea materialListTextArea;
    @FXML    Label clientCounterLabel;
    @FXML    TextField newIncidentNumberTextField;
    @FXML    Button confirmNewIncidentNumberBtn;
    @FXML    Label selectedClientLabel;


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
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
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
        Element newClient = doc.createElement("client");
        newClient.setAttribute("client", newClientName);
        root.appendChild(newClient);
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
    PDFExporter exportFileToPDF = new PDFExporter();
    exportFileToPDF.exportToPDF(client);
    }

    /*Новый протокол сервиса создание*/
    public void newIncidentNumberBtnPressed(ActionEvent actionEvent) {

        if(client!=null&&client.getClientName()!=null){
            confirmNewIncidentNumberBtn.setVisible(true);
            newIncidentNumberTextField.setVisible(true);
            selectedClientLabel.setText(client.getClientName());
        }
        else {
            String headerText = "Не выбран клиент для которого нужно создать новый протокол сервиса";
            String contentText = "Для выбора клиента необходимо нажать кнопку <Вывести список клиентов>, а затем выбрать нужного вам клиента";
            showAlertMessage(headerText, contentText);
        }

    }


    void formSetEditable(boolean textFieldSetEditable){
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

    public void confirmNewIncidentNumberBtn(ActionEvent actionEvent) {
        String newIncidentNumber=newIncidentNumberTextField.getText();

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
                confirmNewIncidentNumberBtn.setVisible(false);
                newIncidentNumberTextField.setVisible(false);

            }
        }
    }

    void showAlertMessage(String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(Main.getPrimaryStage());
        alert.setTitle("Внимание");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
}

