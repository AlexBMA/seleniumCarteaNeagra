package application.helper;

import org.apache.poi.xslf.usermodel.XSLFTextRun;

public class HelperClass {

    public static final long EMU = 914400;
    public static final long INCH_PT = 72;

    public static org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getOutline(XSLFTextRun run) {
        // get underlying CTRegularTextRun object
        org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun ctRegularTextRun = (org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun) run
                .getXmlObject();
        // Are there run properties already? If not, return null.
        if (ctRegularTextRun.getRPr() == null)
            return null;
        // get outline, may be null
        org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties lineProperties = ctRegularTextRun.getRPr()
                .getLn();
        // make a copy to avoid orphaned exceptions or value disconnected exception when
        // set to its own XML parent
        if (lineProperties != null)
            lineProperties = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties) lineProperties.copy();
        return lineProperties;
    }

    public static org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList getEffectList(XSLFTextRun run) {
        // get underlying CTRegularTextRun object
        org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun ctRegularTextRun = (org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun) run
                .getXmlObject();
        // Are there run properties already? If not, return null.
        if (ctRegularTextRun.getRPr() == null)
            return null;
        // get outline, may be null
        org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList effectList = ctRegularTextRun.getRPr()
                .getEffectLst();
        // make a copy to avoid orphaned exceptions or value disconnected exception when
        // set to its own XML parent
        if (effectList != null)
            effectList = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList) effectList.copy();
        return effectList;
    }

    // code partly taken from
    // https://stackoverflow.com/questions/67737897/how-to-add-text-outlines-to-text-within-powerpoint-via-apache-poi

    public static org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties createSolidFillLineProperties(
            java.awt.Color color, double outlineWeight) {
        // create new CTLineProperties
        org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties lineProperties = org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties.Factory
                .newInstance();
        // set line solid fill color
        lineProperties.addNewSolidFill().addNewSrgbClr()
                .setVal(new byte[] { (byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue() }); // outline color
        lineProperties.setW((int) (outlineWeight / INCH_PT * EMU)); // outline weight
        return lineProperties;
    }

    public static org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList createGlow(java.awt.Color color, double radiusPt) {
        // create new CTEffectList
        org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList effectList = org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList.Factory
                .newInstance();
        // add Glow effect
        org.openxmlformats.schemas.drawingml.x2006.main.CTGlowEffect glowEffect = effectList.addNewGlow();
        glowEffect.setRad((int) (radiusPt / INCH_PT * EMU)); // glow radius
        glowEffect.addNewSrgbClr().setVal(new byte[] {(byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue()}); // glow color
        return effectList;
    }
}
