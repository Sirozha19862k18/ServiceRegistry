package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;



public class Controller {

    ObservableList<String> obList;
    Document doc;
    NodeList nodeList;
    int section;
    Node childNodeOfClient;
    NodeList childNode;
    int countMouseClick = 0;
    Element protocolServiseProperties;
    Client client;

    @FXML  ListView<String> list;
    @FXML  TextField numberOfIncident;
    @FXML  TextField dateOfIncidentTextField;
    @FXML  TextArea problemNameTextArea;
    @FXML  TextField employerNameTextField;
    @FXML  TextField carDrivingToIncidentTextField;
    @FXML  TextField employerCounterTextField;
    @FXML  TextField mileageTextField;
    @FXML  TextArea fixProblemTextArea;
    @FXML  TextField employerTimeTextField;

    @FXML
    public void appExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void openXML(ActionEvent actionEvent) throws IOException, SAXException, ParserConfigurationException {
        client = new Client();
        section=1;
        File xmlFille = new File("src/sample/service.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbuilder = factory.newDocumentBuilder();
        doc = dbuilder.parse(xmlFille);
        obList = FXCollections.observableArrayList();
        String elementName="client";
        printElement(elementName);
        list.setItems(obList);
    }
/*Сохранить изменения в протоколе сервиса*/
    @FXML
    public void saveXML(ActionEvent actionEvent) {
   //     if(protocolServiseProperties.hasAttribute("dateIncident")) {
            System.out.println(1);
            protocolServiseProperties.setNodeValue(dateOfIncidentTextField.getText());
protocolServiseProperties.setAttribute("dateIncident", dateOfIncidentTextField.getText());
       // }
        if(protocolServiseProperties.hasAttribute("problemName")) {

        }
        if(protocolServiseProperties.hasAttribute("employerCounter")) {

        }
        if(protocolServiseProperties.hasAttribute("employerName")) {

        }
        if(protocolServiseProperties.hasAttribute("carDrivingToIncident")) {

        }
        if(protocolServiseProperties.hasAttribute("mileage")) {

        }
        if(protocolServiseProperties.hasAttribute("fixProblem")) {

        }
        if(protocolServiseProperties.hasAttribute("employerTime")) {

        }
    }

    /* Вывод списка клиентов */
    void printElement(String elementName) {
         nodeList=doc.getElementsByTagName(elementName);
         for (int i =0; i<nodeList.getLength(); i++) {
          Node node = nodeList.item(i);
             if (node.getNodeType() == Node.ELEMENT_NODE) {
                 Element   eElement = (Element) node;
                 obList.add(eElement.getAttribute(elementName) );
             }
         }

        }
    public void searchChildNode() {
        for (int indx = 0; indx < nodeList.getLength(); indx++)
        {
            Element eElement = (Element) nodeList.item(indx);
            if (eElement.getAttribute("client").equals(client.getClientName()))
            {
                if (nodeList.item(indx).hasChildNodes())
                {
                    childNode = nodeList.item(indx).getChildNodes();
                    for (int i = 0; i < childNode.getLength(); i++)
                    {
                       childNodeOfClient = childNode.item(i);
                        if (childNodeOfClient.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element elementChildOfClient = (Element) childNodeOfClient;
                            obList.add(elementChildOfClient.getAttribute("incidentNumber"));
                        }
                    }
                }
            }
            }
        }

        /* Вывод списка инцидентов */
void incidentDetails(){
    for (int i = 0; i < childNode.getLength(); i++)
    {
        childNodeOfClient = childNode.item(i);
        if (childNodeOfClient.getNodeType() == Node.ELEMENT_NODE)
        {
            Element elementChildOfClient = (Element) childNodeOfClient;
            if (elementChildOfClient.getAttribute("incidentNumber").equals(client.getIncidentNumber()))
            {
                if(elementChildOfClient.hasChildNodes()){
                    NodeList propertiesNode = elementChildOfClient.getChildNodes();
                    for (i=0; i<propertiesNode.getLength(); i++)
                    {
                        Node propNode = propertiesNode.item(i);
                        if(propNode.getNodeType() == Node.ELEMENT_NODE)
                        {
                            protocolServiseProperties = (Element) propNode;
                            incidentPrint (elementChildOfClient);
                        }
                    }
                }
            }
        }
    }
}


public void incidentPrint( Element elementChildOfClient){
    numberOfIncident.setEditable(false);
    numberOfIncident.setDisable(true);
    numberOfIncident.setText(elementChildOfClient.getAttribute("incidentNumber"));
    if(protocolServiseProperties.hasAttribute("dateIncident")) {
        dateOfIncidentTextField.setText(protocolServiseProperties.getAttribute("dateIncident"));
    }
    if(protocolServiseProperties.hasAttribute("problemName")) {
        problemNameTextArea.setText(protocolServiseProperties.getAttribute("problemName"));
    }
    if(protocolServiseProperties.hasAttribute("employerCounter")) {
        employerCounterTextField.setText(protocolServiseProperties.getAttribute("employerCounter"));
    }
    if(protocolServiseProperties.hasAttribute("employerName")) {
        employerNameTextField.setText(protocolServiseProperties.getAttribute("employerName"));
    }
    if(protocolServiseProperties.hasAttribute("carDrivingToIncident")) {
        carDrivingToIncidentTextField.setText(protocolServiseProperties.getAttribute("carDrivingToIncident"));
    }
    if(protocolServiseProperties.hasAttribute("mileage")) {
        mileageTextField.setText(protocolServiseProperties.getAttribute("mileage"));
    }
    if(protocolServiseProperties.hasAttribute("fixProblem")) {
        fixProblemTextArea.setText(protocolServiseProperties.getAttribute("fixProblem"));
    }
    if(protocolServiseProperties.hasAttribute("employerTime")) {
        employerTimeTextField.setText(protocolServiseProperties.getAttribute("employerTime"));
    }

}

        public void listMouseClicked (MouseEvent mouseEvent){
            countMouseClick++;
            if (countMouseClick == 2)
            {
                if(section==1)
                {   //обработка страниц вывода, чтобы по двойному таму на чилдовой ноде не выводился снова список
                    countMouseClick = 0;
                    String clientChange = list.getSelectionModel().getSelectedItem();
                    obList.clear();
                    client.setClientName(clientChange);
                    searchChildNode();
                    section =2;
                }
                if (section == 2)
                {
                    countMouseClick =0;
                    String incidentChange = list.getSelectionModel().getSelectedItem();
                    client.setIncidentNumber(incidentChange);
                    incidentDetails();
                }
                else{
                    countMouseClick =0;}
            }
        }

}
