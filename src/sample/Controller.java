package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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


    @FXML
    ListView<String> list;
    @FXML
    TextField numberOfIncident;
    @FXML
    TextField dateOfIncidentTextField;
    @FXML
    TextArea problemNameTextArea;
    @FXML
    TextField employerNameTextField;
    @FXML
    TextField carDrivingToIncidentTextField;
    @FXML
    TextField employerCounterTextField;
    @FXML
    TextField mileageTextField;
    @FXML
    TextArea fixProblemTextArea;
    @FXML
    TextField employerTimeTextField;
    @FXML
    Button SaveXML;
    @FXML
    Button addClientWindowCloseBtn;
    @FXML
    TextField newClientNameTextField;
    @FXML
    Button addClientToBaseBtn;


    @FXML
    public void appExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void loadXMLfile() throws ParserConfigurationException, IOException, SAXException {
        File xmlFille = new File("src/sample/service.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbuilder = factory.newDocumentBuilder();
        doc = dbuilder.parse(xmlFille);
    }

    @FXML
    public void openXML(ActionEvent actionEvent) throws IOException, SAXException, ParserConfigurationException {
        client = new Client();
        section = 1;
        loadXMLfile();
        observableListClient = FXCollections.observableArrayList();
        observableListIncident = FXCollections.observableArrayList();
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
        nodeList = doc.getElementsByTagName("client");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                observableListClient.add(eElement.getAttribute("client"));
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
}

