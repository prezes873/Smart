package pl.wasat.smarthma.model.iso;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

import pl.wasat.smarthma.utils.text.SmartHMAStringStyle;

public class MDLegalConstraints implements Serializable {

    private static final long serialVersionUID = 1L;

    private AccessConstraints accessConstraints;
    private OtherConstraints otherConstraints;
    private String Prefix;


    /**
     * @return The accessConstraints
     */
    public AccessConstraints getAccessConstraints() {
        return accessConstraints;
    }

    /**
     * @param accessConstraints The accessConstraints
     */
    public void setAccessConstraints(AccessConstraints accessConstraints) {
        this.accessConstraints = accessConstraints;
    }

    /**
     * @return The otherConstraints
     */
    public OtherConstraints getOtherConstraints() {
        return otherConstraints;
    }

    /**
     * @param otherConstraints The otherConstraints
     */
    public void setOtherConstraints(OtherConstraints otherConstraints) {
        this.otherConstraints = otherConstraints;
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
