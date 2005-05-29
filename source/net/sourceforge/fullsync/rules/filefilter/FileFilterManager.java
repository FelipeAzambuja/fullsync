/*
 * Created on May 29, 2005
 */
package net.sourceforge.fullsync.rules.filefilter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author Michele Aiello
 */
public class FileFilterManager {
	
	public Element serializeFileFilter(FileFilter fileFilter, Document document) {
		Element filterElement = document.createElement("FileFilter");
		document.appendChild(filterElement);
		filterElement.setAttribute("matchtype", String.valueOf(fileFilter.getMatchType()));

		FileFilterRule[] rules = fileFilter.getFileFiltersRules();
		for (int i = 0; i < rules.length; i++) {
			Element ruleElement = serializeRule(rules[i], document);
			filterElement.appendChild(ruleElement);
		}
		
		return filterElement;
	}

	public Element serializeRule(FileFilterRule fileFilterRule, Document document) {
		Element ruleElement = document.createElement("FileFilterRule");
		String ruleType = getRuleType(fileFilterRule);
		
		ruleElement.setAttribute("ruletype", ruleType);
		serializeRuleAttributes(fileFilterRule, ruleElement);
		
		String desc = fileFilterRule.toString();
		Element descriptionElement = document.createElement("Description");
		Text descNode = document.createTextNode(desc);
		descriptionElement.appendChild(descNode);
		ruleElement.appendChild(descriptionElement);
		
		return ruleElement;
	}
	
	public FileFilter unserializeFileFilter(Element fileFilterElement) {
		FileFilter fileFilter = new FileFilter();
		int match_type = -1;

		try {
			match_type = Integer.parseInt(fileFilterElement.getAttribute("matchtype"));
		} catch (NumberFormatException e) {
		}
		fileFilter.setMatchType(match_type);
		
		NodeList ruleList = fileFilterElement.getElementsByTagName("FileFilterRule");
		int numOfRules = ruleList.getLength();
		FileFilterRule[] rules = new FileFilterRule[numOfRules];
		
		for (int i = 0; i < rules.length; i++) {
			rules[i] = unserializeFileFilterRule((Element)ruleList.item(i));
		}
		
		fileFilter.setFileFilterRules(rules);
		
		return fileFilter;
	}

	public FileFilterRule unserializeFileFilterRule(Element fileFilterRuleElement) {
		FileFilterRule rule = null;
		String ruleType = fileFilterRuleElement.getAttribute("ruletype");
		
		if (ruleType.equals("FileName")) {
			int op = Integer.parseInt(fileFilterRuleElement.getAttribute("op"));
			String pattern = fileFilterRuleElement.getAttribute("pattern");
			rule = new FileNameFileFilterRule(pattern, op);
		}
		
		if (ruleType.equals("FileSize")) {
			int op = Integer.parseInt(fileFilterRuleElement.getAttribute("op"));
			int size = Integer.parseInt(fileFilterRuleElement.getAttribute("size"));
			rule = new FileSizeFileFilterRule(size, op);
		}

		if (ruleType.equals("FileModificationDate")) {
			int op = Integer.parseInt(fileFilterRuleElement.getAttribute("op"));
			int millis = Integer.parseInt(fileFilterRuleElement.getAttribute("modificationdate"));
			rule = new FileModificationDateFileFilterRule(millis , op);
		}

		return rule;
	}
	
	private String getRuleType(FileFilterRule fileFilterRule) {
		if (fileFilterRule.getClass().equals(FileNameFileFilterRule.class)) {
			return "FileName";
		}
		if (fileFilterRule.getClass().equals(FileSizeFileFilterRule.class)) {
			return "FileSize";
		}
		if (fileFilterRule.getClass().equals(FileModificationDateFileFilterRule.class)) {
			return "FileModificationDate";
		}
		return null;
	}
	
	private void serializeRuleAttributes(FileFilterRule fileFilterRule, Element ruleElement) {
		if (fileFilterRule.getClass().equals(FileNameFileFilterRule.class)) {
			FileNameFileFilterRule rule = (FileNameFileFilterRule) fileFilterRule;
			ruleElement.setAttribute("op", String.valueOf(rule.getOperator()));
			ruleElement.setAttribute("pattern", rule.getPattern());
		}
		if (fileFilterRule.getClass().equals(FileSizeFileFilterRule.class)) {
			FileSizeFileFilterRule rule = (FileSizeFileFilterRule) fileFilterRule;
			ruleElement.setAttribute("op", String.valueOf(rule.getOperator()));
			ruleElement.setAttribute("size", String.valueOf(rule.getSize()));
		}
		if (fileFilterRule.getClass().equals(FileModificationDateFileFilterRule.class)) {
			FileModificationDateFileFilterRule rule = (FileModificationDateFileFilterRule) fileFilterRule;
			ruleElement.setAttribute("op", String.valueOf(rule.getOperator()));
			ruleElement.setAttribute("modificationdate", String.valueOf(rule.getModificationDate()));
		}
	}
}