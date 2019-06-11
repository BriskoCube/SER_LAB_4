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

public class InterfaceRecherchePays extends JFrame {

    private JPanel panelRecherche = new JPanel(new FlowLayout());

    private JComboBox<Object> continents;
    private JComboBox<Object> langages;
    private JButton createXSL = new JButton("Générer XSL");
    private JTextField superficieMin = new JTextField(5);
    private JTextField superficieMax = new JTextField(5);

    public InterfaceRecherchePays(File xmlFile) {

        try{
            InputStream in = new FileInputStream(xmlFile);
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(in);


            XPathFactory xpath = XPathFactory.instance();

            Set<String> languages = xPathQuery("/countries/element/languages/element/name", xpath, document);
            Set<String> continents = xPathQuery("/countries/element/region", xpath, document);

            this.langages = new JComboBox<Object>(languages.toArray());
            this.continents = new JComboBox<Object>(continents.toArray());



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

                        output.addContent(html);

                        Document xslDocument = new Document(stylesheet);


                        //JDOM document is ready now, lets write it to file now
                        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
                        //output xml to console for debugging
                        //xmlOutputter.output(doc, System.out);
                        xmlOutputter.output(xslDocument, new FileOutputStream("test.xsl"));


                        // Utiliser les namespace pour xsl

                        // Création des fichiers XSL selon ce qui est demandé

                        /** A compléter... **/
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }



                }

            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }




        /**
         * A compléter : Remplissage des listes de recherche (avec les continents et les langues parlées dans l'ordre alphabétique)
         */

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

        Element colDiv = generateDivElement("col-2 mt-2 mx-auto");

        Element button = new Element("button");
        button.setAttribute("class", "btn btn-light");
        button.setAttribute("data-toggle", "modal");

        Element attribute = new Element("attribute", xslt);
        attribute.setAttribute("name", "data-target");
        attribute.setText(".modal-<xsl:value-of select=\"alpha3Code\" />");

        Element valueOf = new Element("value-of", xslt);
        valueOf.setAttribute("select", "name");

        Element img = new Element("img");
        img.setAttribute("class", "flag pl-1");

        Element attributeSrc = new Element("attribute", xslt);
        Element valueOfFlag = new Element("value-of", xslt);
        valueOfFlag.setAttribute("select", "flag");

        attributeSrc.setContent(attributeSrc);

        button.addContent(attribute);
        button.addContent(valueOf);
        button.addContent(img);
        colDiv.addContent(button);
        forEach.addContent(colDiv);
        rowDiv.addContent(forEach);
        containersDiv.addContent(rowDiv);
        body.addContent(containersDiv);

        return body;
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
        head.addContent(scriptBootstrap);
        head.addContent(scriptJquery);
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
