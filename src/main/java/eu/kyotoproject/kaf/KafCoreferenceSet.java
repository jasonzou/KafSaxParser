package eu.kyotoproject.kaf;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 11/2/12
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class KafCoreferenceSet {

    /*
    <coreferences>
<coref id="co1">
<!-- London -->
<span >
<target id="t12" head="yes"/>
</span>
<!-- the capital city of England -->
<span>
<target id="t1"/>
<target id="t2"/>
<target id="t3" head ="yes"/> <!-- city is the head -->
<target id="t4"/>
<target id="t5"/>
</span>
<!-- it -->
<span>
<target id="t20" head="yes"/>
</span>
</coref>
</coreferences>
     */

    private String coid;
    private String type;
    private ArrayList<ArrayList<CorefTarget>> setsOfSpans;


    public KafCoreferenceSet() {
        this.coid = "";
        this.type = "";
        this.setsOfSpans = new ArrayList<ArrayList<CorefTarget>>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoid() {
        return coid;
    }

    public void setCoid(String coid) {
        this.coid = coid;
    }

    public ArrayList<ArrayList<CorefTarget>> getSetsOfSpans() {
        return setsOfSpans;
    }

    public void setSetsOfSpans(ArrayList<ArrayList<CorefTarget>> setsOfSpans) {
        this.setsOfSpans = setsOfSpans;
    }

    public void addSetsOfSpans(ArrayList<CorefTarget> setOfSpans) {
        this.setsOfSpans.add(setOfSpans);
    }

    @Override
    public String toString() {
        return "KafCoreferenceSet{" +
                "coid='" + coid + '\'' +
                "type='" + type + '\'' +
                ", setsOfSpans=" + setsOfSpans +
                '}';
    }

    public Element toXML(Document xmldoc)
    {
        Element root = xmldoc.createElement("coref");
        if (coid != null)
            root.setAttribute("coid", coid);
        if (type != null)
            root.setAttribute("type", type);
        if (setsOfSpans.size()>0) {
            for (int i = 0; i < setsOfSpans.size(); i++) {
                ArrayList<CorefTarget> corefTargets = setsOfSpans.get(i);
                Element setOfTargets = xmldoc.createElement(("span"));
                for (int j = 0; j < corefTargets.size(); j++) {
                    CorefTarget corefTarget = corefTargets.get(j);
                    setOfTargets.appendChild(corefTarget.toXML(xmldoc));
                }
                root.appendChild(setOfTargets);
            }
        }
        return root;
    }
    public Element toNafXML(Document xmldoc)
    {
        Element root = xmldoc.createElement("coref");
        if (coid != null)
            root.setAttribute("id", coid);
        if (type != null)
            root.setAttribute("type", type);
        if (setsOfSpans.size()>0) {
            for (int i = 0; i < setsOfSpans.size(); i++) {
                ArrayList<CorefTarget> corefTargets = setsOfSpans.get(i);
                Element setOfTargets = xmldoc.createElement(("span"));

                String tokenString = "";
                for (int j = 0; j < corefTargets.size(); j++) {
                    CorefTarget corefTarget = corefTargets.get(j);
                    tokenString += " "+corefTarget.getTokenString();

                }
                if (tokenString.trim().length()>0) {
                    Comment comment = xmldoc.createComment(tokenString.trim());
                    setOfTargets.appendChild(comment);
                }

                for (int j = 0; j < corefTargets.size(); j++) {
                    CorefTarget corefTarget = corefTargets.get(j);
                    setOfTargets.appendChild(corefTarget.toXML(xmldoc));
                }
                root.appendChild(setOfTargets);
            }
        }
        return root;
    }
}
