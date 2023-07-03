package com.example.Masterproject4;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

@RestController
public class HtmlController {

    private final ResourceLoader resourceLoader;

    public HtmlController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "") String name) {
        return "<h1>Hello " + name + "!</h1>\n" + new java.util.Date();
    }

    @PostMapping("/submit-form")
    public String submitForm(@RequestParam("Temperatur") String Temperatur,
                             @RequestParam("Luftfeuchtigkeit") String Luftfeuchtigkeit,
                             @RequestParam("Pneumatikversorgung") String Pneumatikversorgung,
                             @RequestParam("Stromversorgung") String Stromversorgung,
                             @RequestParam("Flächenkosten") String Flächenkosten,
                             @RequestParam("Einsatzdauer") String Einsatzdauer,
                             @RequestParam("Ressourcen") MultipartFile RessourcenFile,
                             @RequestParam("file") MultipartFile file) throws IOException {


        System.out.println("Temperatur: " + Temperatur);
        System.out.println("Luftfeuchtigkeit: " + Luftfeuchtigkeit);
        System.out.println("Pneumatikversorgung: " + Pneumatikversorgung);
        System.out.println("Stromversorgung: " + Stromversorgung);
        System.out.println("Flächenkosten: " + Flächenkosten);
        System.out.println("Einsatzdauer: " + Einsatzdauer);
        System.out.println("Filetyp: " + file.getContentType());


        String path = "file:/C:/Users/domin/Desktop/Wilhelm Büchner - Hochschule/Masterarbeit/AASX_Files/*.json";
        // Dateien einlesen
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
        Resource[] resources = resolver.getResources(path);


        for (Resource resource : resources) {
            if (resource.isReadable()) {
                System.out.println("Datei: " + resource.getFilename());
            }
        }

        try {
            if (file.getContentType().equals("application/json")) {
                // JSON-Datei verarbeiten
                String jsonData = new String(file.getBytes());
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray idShortArray = jsonObject.getJSONArray("kind");

                for (int i = 0; i < idShortArray.length(); i++) {
                    JSONObject aasObj = idShortArray.getJSONObject(i);
                    String idShort = aasObj.getString("idShort");
                    int value = aasObj.getInt("value");
                    System.out.println("IdShort: " + idShort + ", Value: " + value);
                }
                System.out.println(jsonData);
                // Führe die entsprechenden Operationen mit den JSON-Daten aus
                return "JSON-Datei erfolgreich verarbeitet.";
            } else if (file.getContentType().equals("text/xml")) {

                // XML-Datei verarbeiten
                String xmlData = new String(file.getBytes());
                // Erstellung eines Document-Objekts aus XML-Datei
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));
                // Liste aller Element von aas:property
                NodeList nodeList = document.getElementsByTagName("aas:property");


                // Neue Instanz der Produktanforderung (Reflexion) ggf. Reflection API um Klasse zu erstellen
                ProductRequirement productRequirement = new ProductRequirement();
                Class<?> clazz = ProductRequirement.class;
                Field[] fields = clazz.getDeclaredFields();

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        String idShort = element.getElementsByTagName("aas:idShort").item(0).getTextContent();
                        String value = element.getElementsByTagName("aas:value").item(0).getTextContent();
                        System.out.println("idShort: " + idShort + ", value: " + value);
                        //
                        for (Field field : fields) {
                            if (field.getName().equals(idShort)) {
                                field.setAccessible(true);
                                try {
                                    Field field1 = productRequirement.getClass().getDeclaredField(idShort);
                                    Class<?> fieldType = field1.getType();
                                    if (fieldType == int.class || fieldType == Integer.class) {
                                        field.setInt(productRequirement, Integer.parseInt(value));
                                    } else {
                                        field.set(productRequirement, value);
                                    }
                                } catch (IllegalAccessException | NoSuchFieldException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                    }
                }
                // Führe die entsprechenden Operationen mit den XML-Daten aus
                return "XML-Datei erfolgreich verarbeitet.";
            } else {
                return "Nur JSON- und XML-Dateien werden akzeptiert.";
            }
        } catch (IOException e) {
            return "Fehler beim Verarbeiten der Datei.";

        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

    }

}
