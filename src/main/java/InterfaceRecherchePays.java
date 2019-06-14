import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InterfaceRecherchePays extends JFrame {

    private JPanel panelRecherche = new JPanel(new FlowLayout());

    private JComboBox<String> continents = new JComboBox<>();
    private JComboBox<String> langages = new JComboBox<>();
    private JButton createXSL = new JButton("Générer XSL");
    private JTextField superficieMin = new JTextField(5);
    private JTextField superficieMax = new JTextField(5);

    public InterfaceRecherchePays(File xmlFile) {

        createXSL.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {


                super.mouseClicked(e);


                try{

                    Namespace xslt = Namespace.getNamespace("xsl", "http://www.w3.org/1999/XSL/Transform");


                    Element stylesheet = new Element("stylesheet", xslt);

                    stylesheet.setAttribute("version", "1.0");

                    // output
                    Element output = new Element("output", xslt);
                    output.setAttribute("method", "html").setAttribute("encoding", "UTF-8");
                    output.setAttribute("doctype-public", "-//W3C//DTD HTML 4.01//EN");
                    output.setAttribute(  "doctype-system" ,"http://www.w3.org/TR/html4/strict.dtd");
                    output.setAttribute("indent", "yes");
                    stylesheet.addContent(output);

                    // template
                    Element template = new Element("template", xslt);
                    template.setAttribute("match", "/");

                    // html
                    Element html = new Element("html");

                    // head
                    Element head = generateHead();

                    // body
                    Element body = generateBody(xslt);

                    html.addContent(head);
                    html.addContent(body);

                    template.addContent(html);
                    stylesheet.addContent(template);

                    Document xslDocument = new Document(stylesheet);


                    //JDOM document is ready now, lets write it to file now
                    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
                    //output xml to console for debugging
                    //xmlOutputter.output(doc, System.out);
                    xmlOutputter.output(xslDocument, new FileOutputStream("countries.xsl"));


                    // Utiliser les namespace pour xsl

                    // Création des fichiers XSL selon ce qui est demandé

                    /** A compléter... **/
                } catch (IOException e1) {
                    e1.printStackTrace();
                }



            }

        });


        /**
         * A compléter : Remplissage des listes de recherche (avec les continents et les langues parlées dans l'ordre alphabétique)
         */

        SAXBuilder builder = new SAXBuilder();
        Document document = null;

        try {
            // parse and load file into memory
            InputStream in = new BufferedInputStream(new FileInputStream(xmlFile));
            document = builder.build(in);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create xpath factory
        XPathFactory xpath = XPathFactory.instance();

        System.out.println("1. select all continents");
        XPathExpression<Element> expr = xpath.compile("//region", Filters.element());
        java.util.List<Element> continentsDuplicate = expr.evaluate(document);
        java.util.List<String> continentNames = continentsDuplicate.stream()
                .map(element -> element.getValue().trim())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        for (String continent : continentNames){
            if(!continent.isEmpty()) {
                continents.addItem(continent);
            }
        }

        System.out.println("1. select all languages");
        XPathExpression<Element> exprLanguage = xpath.compile("//languages/element/name", Filters.element());
        java.util.List<Element> languageDuplicate = exprLanguage.evaluate(document);
        java.util.List<String> languageNames = languageDuplicate.stream()
                .map(element -> element.getValue().trim())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        for (String language : languageNames){
            if(!language.isEmpty()) {
                langages.addItem(language);
            }
        }

        setLayout(new BorderLayout());

        panelRecherche.add(new JLabel("Choix d'un continent"));
        panelRecherche.add(continents);

        panelRecherche.add(new JLabel("Choix d'une langue"));
        panelRecherche.add(langages);

        panelRecherche.add(new JLabel("Superficie minimume"));
        panelRecherche.add(superficieMin);

        panelRecherche.add(new JLabel("Superficie maximum"));
        panelRecherche.add(superficieMax);

        panelRecherche.add(createXSL);

        add(panelRecherche, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("Interface de recherche de pays");


    }

    private Element generateBody(Namespace xslt) {
        Element body = new Element("body");
        // containers
        Element containersDiv = generateDivElement("containers");
        Element rowDiv = generateDivElement("row");

        Element forEach = new Element("for-each", xslt);
        forEach.setAttribute("select", "countries/element");

        // Génération des flags
        generateFlags(xslt, forEach);
        generateModals(xslt, forEach);

        rowDiv.addContent(forEach);
        containersDiv.addContent(rowDiv);
        body.addContent(containersDiv);

        return body;
    }

    private void generateModals(Namespace xslt, Element forEach) {
        // Génération des modals
        Element div = generateDivElement("modal fade bd-example-modal-sm");
        div.setAttribute("tabindex", "-1");
        div.setAttribute("role", "dialog");
        div.setAttribute("aria-labelledby", "mySmallModalLabel");
        div.setAttribute("aria-hidden","true");

        Element attributeModal = new Element("attribute", xslt);
        attributeModal.setAttribute("name", "class");
        Element valueOfAlpha = new Element("value-of", xslt);
        valueOfAlpha.setAttribute("select", "concat('modal fade modal-',alpha3Code)");
        attributeModal.addContent(valueOfAlpha);

        // Modal
        Element divModalDial = generateDivElement("modal-dialog modal-md");
        Element divModalCont = generateDivElement("modal-content");

        // Modal Header
        Element divModalHeader = generateDivElement("modal-header");
        Element h5ModalTitle = new Element("h5");
        h5ModalTitle.setAttribute("class", "modal-title");
        h5ModalTitle.setAttribute("id", "exampleModalLabel");
        Element valueOfTransl = new Element("value-of", xslt);
        valueOfTransl.setAttribute("select", "translations/fr");

        h5ModalTitle.addContent(valueOfTransl);
        divModalHeader.addContent(h5ModalTitle);
        divModalCont.addContent(divModalHeader);

        // Modal body
        Element divModalBody = generateDivElement("modal-body");
        Element divRow = generateDivElement("row");
        Element divCol = generateDivElement("col-6");
        Element imgW = new Element("img").setAttribute("class", "w-100");
        Element attributeSrc = new Element("attribute", xslt).setAttribute("name","src");
        Element valueOfFlag = new Element("value-of", xslt).setAttribute("select","flag");

        // General Infos
        Element divCol2 = generateDivElement("col-6");
        Element pCapitale = new Element("p");
        pCapitale.addContent(new Element("value-of", xslt).setAttribute("select", "concat('Capitale: ', capitale)"));
        Element pPopulation = new Element("p");
        pPopulation.addContent(new Element("value-of", xslt).setAttribute("select", "concat('Population: ', population, ' habitants')"));
        Element pSuperficie = new Element("p");
        pSuperficie.addContent(new Element("value-of", xslt).setAttribute("select", "concat('Superficie: ', area, ' km²')"));
        Element pContinent = new Element("p");
        pContinent.addContent(new Element("value-of", xslt).setAttribute("select", "concat('Continent: ', region)"));
        Element pSousContinent = new Element("p");
        pSousContinent.addContent(new Element("value-of", xslt).setAttribute("select", "concat('Sous-continent: ', subregion)"));

        // Languages
        Element divCol12 = generateDivElement("col-12");
        Element divCard = generateDivElement("card bg-light mb-3 w-100");
        Element divCardHeader = generateDivElement("card-header").setText("Langues Parlées");
        Element divCardBody = generateDivElement("card-body");
        Element ul = new Element("ul").setAttribute("class", "list-group");
        Element xslForEach = new Element("for-each", xslt).setAttribute("select","languages/element");
        Element li = new Element("li").setAttribute("class", "list-group-item");
        Element valueOfName = new Element("value-of", xslt).setAttribute("select", "name");

        // Modal Footer
        Element modalFooter = generateDivElement("modal-footer");
        Element button = new Element("button");
        button.setAttribute("type", "button");
        button.setAttribute("class", "btn btn-primary");
        button.setAttribute("data-dismiss", "modal");
        button.setText("Fermer");

        modalFooter.addContent(button);

        divModalCont.addContent(divModalBody);
        divModalCont.addContent(modalFooter);
        li.addContent(valueOfName);
        xslForEach.addContent(li);
        ul.addContent(xslForEach);
        divCardBody.addContent(ul);
        divCard.addContent(divCardHeader);
        divCard.addContent(divCardBody);
        divCol12.addContent(divCard);

        divCol2.addContent(pCapitale);
        divCol2.addContent(pPopulation);
        divCol2.addContent(pSuperficie);
        divCol2.addContent(pContinent);
        divCol2.addContent(pSousContinent);

        attributeSrc.addContent(valueOfFlag);
        imgW.addContent(attributeSrc);
        divCol.addContent(imgW);
        divRow.addContent(divCol);
        divRow.addContent(divCol2);
        divRow.addContent(divCol12);
        divModalBody.addContent(divRow);

        divModalDial.addContent(divModalCont);
        div.addContent(attributeModal);
        div.addContent(divModalDial);
        forEach.addContent(div);
    }

    private void generateFlags(Namespace xslt, Element forEach) {
        Element colDiv = generateDivElement("col-2 mt-2 mx-auto");

        Element button = new Element("button");
        button.setAttribute("class", "btn btn-light");
        button.setAttribute("data-toggle", "modal");

        Element attribute = new Element("attribute", xslt);
        attribute.setAttribute("name", "data-target");
        Element valueOfAlpha = new Element("value-of", xslt);
        valueOfAlpha.setAttribute("select", "concat('.modal-',alpha3Code)");
        attribute.addContent(valueOfAlpha);

        Element valueOf = new Element("value-of", xslt);
        valueOf.setAttribute("select", "translations/fr");

        Element img = new Element("img");
        img.setAttribute("class", "flag pl-1");

        Element attributeSrc = new Element("attribute", xslt);
        attributeSrc.setAttribute("name", "src");
        Element valueOfFlag = new Element("value-of", xslt);
        valueOfFlag.setAttribute("select", "flag");

        attributeSrc.addContent(valueOfFlag);
        img.addContent(attributeSrc);
        button.addContent(attribute);
        button.addContent(valueOf);
        button.addContent(img);
        colDiv.addContent(button);
        forEach.addContent(colDiv);
    }

    private Element generateHead() {
        Element head = new Element("head");
        // import des CSS et JS pour bootstrap
        Element linkBootstrap = new Element("link");
        linkBootstrap.setAttribute("rel", "stylesheet");
        linkBootstrap.setAttribute("href", "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css");
        linkBootstrap.setAttribute("integrity", "sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T");
        linkBootstrap.setAttribute("crossorigin", "anonymous");

        Element scriptJquery = new Element("script");
        scriptJquery.setAttribute("src", "https://code.jquery.com/jquery-3.3.1.slim.min.js");
        scriptJquery.setAttribute("integrity", "sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo");
        scriptJquery.setAttribute("crossorigin", "anonymous");

        Element scriptPopper = new Element("script");
        scriptPopper.setAttribute("src", "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js");
        scriptPopper.setAttribute("integrity", "sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1");
        scriptPopper.setAttribute("crossorigin", "anonymous");

        Element scriptBootstrap = new Element("script");
        scriptBootstrap.setAttribute("src", "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js");
        scriptBootstrap.setAttribute("integrity", "sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM");
        scriptBootstrap.setAttribute("crossorigin", "anonymous");

        Element style = new Element("style");
        style.setText(".flag{max-width:20px;}");

        head.addContent(style);
        head.addContent(linkBootstrap);
        head.addContent(scriptJquery);
        head.addContent(scriptBootstrap);
        head.addContent(scriptPopper);
        return head;
    }

    private Element generateDivElement(String attribute){
        Element div = new Element("div");
        div.setAttribute("class", attribute);
        return div;
    }

    public Set<String> xPathQuery(String expressions, XPathFactory xpath, Document document){
        XPathExpression<Element> expr = xpath.compile(expressions, Filters.element());

        List<Element> duplicatedElements = expr.evaluate(document);

        Set<String> elements = new HashSet<>();

        elements.add("");

        for (Element element : duplicatedElements){
            elements.add(element.getValue().trim());
        }

        return elements;
    }

    public static void main(String ... args) {

        new InterfaceRecherchePays(new File("countries.xml"));

    }

}
