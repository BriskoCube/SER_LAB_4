import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashSet;
import java.util.List;
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




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }


        createXSL.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);

                //distinct-values(/countries/element/languages/element/name)

                //distinct-values(/countries/element/region)







                // Création des fichiers XSL selon ce qui est demandé

                /** A compléter... **/

            }

        });

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
