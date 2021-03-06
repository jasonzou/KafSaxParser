package eu.kyotoproject.kaf;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 2-apr-2009
 * Time: 11:18:29
 * To change this template use File | Settings | File Templates.
 * This file is part of KafSaxParser.

    KafSaxParser is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    KafSaxParser is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with KafSaxParser.  If not, see <http://www.gnu.org/licenses/>.
 */
public class KafSense  implements Serializable {
    /*
    <sense sensecode="ENG-30-04751652-n" confidence="1.0"/>
    <externalRef resource="WN-1.3" reference="" "ENG-30-04751652-n" confidence="1.0"/>
     */
    /*
    @TODO remove serialVersionUID
    java.io.InvalidClassException: eu.kyotoproject.kaf.KafSense;
    local class incompatible: stream classdesc serialVersionUID = 8053939673306041625,
    local class serialVersionUID = -8445028541396433418
     */
    private static final long serialVersionUID = 8053939673306041625L;

    static public final String LOWEST_COMMON_SUBSUMER = "lowest_common_subsumer";
    static public final String HYPERNYM = "hypernym";
    static public final String DOMINANT_SENSE = "dominant_sense";

    private String resource;
    private String source;
    private String sensecode;
    private double confidence;
    private String refType = "", status = "";
    private ArrayList<KafSense> children;
    private KafTermSentiment kafTermSentiment;

    public KafSense() {
        this.kafTermSentiment = new KafTermSentiment();
        this.sensecode = "";
        this.source = "";
        this.resource = "";
        this.confidence = 0;
        this.children = new ArrayList<KafSense>();
    }
    
    public KafSense(String resource, String souce, String reference, String refType, String status)
    {
    	this();
    	if (source!=null)
            this.source = source;
        if (resource != null)
    		this.resource = resource;
    	if (reference != null)
    		this.sensecode = reference;
    	if (refType != null)
    		this.refType = refType;
    	if (status != null)
    		this.status = status;
        this.children = new ArrayList<KafSense>();
    }

    public String toString() {
        String str = "";
        str += "<externalRef resource=\""+resource+"\" reference=\""+sensecode+"\" confidence=\""+confidence+"\"";
        if (kafTermSentiment.hasValue()) {
            str += ">\n";
            str += kafTermSentiment.toString();
            str += "</externalRef>\n";
        }
        else {
            str += "/>\n";
        }
        return str;
    }

    public Element toXML(Document xmldoc)
    {
  	  Element root = xmldoc.createElement("externalRef");
      root.setAttribute("resource", resource);
      if (!sensecode.isEmpty()) root.setAttribute("reference", sensecode);
      if (!source.isEmpty()) root.setAttribute("source", source);
      if (!refType.isEmpty()) root.setAttribute("reftype", refType);
      if (!status.isEmpty()) root.setAttribute("status", status);
      if (confidence>0) root.setAttribute("confidence", Double.toString(confidence));
      if (kafTermSentiment.hasValue()) {
          root.appendChild(kafTermSentiment.toXML(xmldoc));
      }
      if (children.size()>0) {
          for (int i = 0; i < children.size(); i++) {
              KafSense kafSense = children.get(i);
              root.appendChild(kafSense.toXML(xmldoc));
          }
      }
  	  return root;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ArrayList<KafSense> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<KafSense> children) {
        this.children = children;
    }

    public void addChildren(KafSense child) {
        this.children.add(child);
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getSensecode() {
        return sensecode;
    }

    public void setSensecode(String sensecode) {
        this.sensecode = sensecode;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public KafTermSentiment getKafTermSentiment() {
        return kafTermSentiment;
    }

    public void setKafTermSentiment(KafTermSentiment kafTermSentiment) {
        this.kafTermSentiment = kafTermSentiment;
    }

    static void addSenseToSenseTags (ArrayList<KafSense> senseTags, int level, KafSense sense) {
        if (level == 0 || senseTags.size()==0) {
            senseTags.add(sense);
        }
        else {
            KafSense k = senseTags.get(senseTags.size() - 1);
            if (level==1) {
                // System.out.println("k.toString() = " + k.toString());
                k.getChildren().add(sense);
            }
            else {
                addSenseToSenseTags(k.children, level-1, sense);
            }
        }
    }
}
