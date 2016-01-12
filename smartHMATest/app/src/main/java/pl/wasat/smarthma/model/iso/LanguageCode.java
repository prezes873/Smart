package pl.wasat.smarthma.model.iso;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

import pl.wasat.smarthma.utils.text.SmartHMAStringStyle;

public class LanguageCode implements Serializable {

    private static final long serialVersionUID = 1L;

    private String CodeList;
    private String CodeListValue;
    private String Prefix;
    private String Text;


    /**
     * @return The CodeList
     */
    public String getCodeList() {
        return CodeList;
    }

    /**
     * @param CodeList The _codeList
     */
    public void setCodeList(String CodeList) {
        this.CodeList = CodeList;
    }

    /**
     * @return The CodeListValue
     */
    public String getCodeListValue() {
        return CodeListValue;
    }

    /**
     * @param CodeListValue The _codeListValue
     */
    public void setCodeListValue(String CodeListValue) {
        this.CodeListValue = CodeListValue;
    }

    /**
     * @return The Prefix
     */
    public String getPrefix() {
        return Prefix;
    }

    /**
     * @param Prefix The _prefix
     */
    public void setPrefix(String Prefix) {
        this.Prefix = Prefix;
    }

    /**
     * @return The Text
     */
    public String getText() {
        return Text;
    }

    /**
     * @param Text The _text
     */
    public void setText(String Text) {
        this.Text = Text;
    }

    @Override
    public String toString() {
        ToStringStyle style = new SmartHMAStringStyle();
        ToStringBuilder.setDefaultStyle(style);
        return ToStringBuilder.reflectionToString(this, style);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
}
