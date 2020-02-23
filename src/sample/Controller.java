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

    @FXML  ListView<String> list;
    @FXML  TextField numberOfIncident;
    @FXML  TextField dateOfIncidentTextField;
    @FXML  TextArea problemNameTextArea;
    @FXML  TextField employerNameTextField;
    @FXML  TextField carDrivingToIncidentTextField;
    @FXML  TextField employerCounterTextField;
    @FXML  TextField mileageTextField;
    @FXML  TextArea fixProblemTextArea;

    @FXML
    public void appExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void openXML(ActionEvent actionEvent) throws IOException, SAXException, ParserConfigurationException {
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
    public void searchChildNode(String clientChange) {
        for (int indx = 0; indx < nodeList.getLength(); indx++)
        {
            Element eElement = (Element) nodeList.item(indx);
            if (eElement.getAttribute("client").equals(clientChange))
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
void incidentDetails(String incidentChange){
    for (int i = 0; i < childNode.getLength(); i++)
    {
        childNodeOfClient = childNode.item(i);
        if (childNodeOfClient.getNodeType() == Node.ELEMENT_NODE)
        {
            Element elementChildOfClient = (Element) childNodeOfClient;
            if (elementChildOfClient.getAttribute("incidentNumber").equals(incidentChange))
            {
                if(elementChildOfClient.hasChildNodes()){
                    NodeList propertiesNode = elementChildOfClient.getChildNodes();
                    for (i=0; i<propertiesNode.getLength(); i++)
                    {
                        Node propNode = propertiesNode.item(i);
                        if(propNode.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element prop = (Element) propNode;
                            incidentPrint(prop, elementChildOfClient);
                        }
                    }
                }
            }
        }
    }
}


public void incidentPrint(Element prop, Element elementChildOfClient){
    numberOfIncident.setText(elementChildOfClient.getAttribute("incidentNumber"));
    if(prop.hasAttribute("dateIncident")) {
        dateOfIncidentTextField.setText(prop.getAttribute("dateIncident"));
    }
    if(prop.hasAttribute("problemName")) {
        problemNameTextArea.setText(prop.getAttribute("problemName"));
    }
    if(prop.hasAttribute("employerCounter")) {
        employerCounterTextField.setText(prop.getAttribute("employerCounter"));
    }
    if(prop.hasAttribute("employerName")) {
        employerNameTextField.setText(prop.getAttribute("employerName"));
    }
    if(prop.hasAttribute("carDrivingToIncident")) {
        carDrivingToIncidentTextField.setText(prop.getAttribute("carDrivingToIncident"));
    }
    if(prop.hasAttribute("mileage")) {
        mileageTextField.setText(prop.getAttribute("mileage"));
    }
    if(prop.hasAttribute("fixProblem")) {
        fixProblemTextArea.setText(prop.getAttribute("fixProblem"));
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
                    searchChildNode(clientChange);
                    section =2;
                }
                if (section == 2)
                {
                    countMouseClick =0;
                    String incidentChange = list.getSelectionModel().getSelectedItem();
                    incidentDetails(incidentChange);
                }
                else{
                    countMouseClick =0;}
            }
        }
    }
