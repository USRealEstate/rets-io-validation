/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/terms/DateTerm.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.terms;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;


/**
 *  DateTerm.java Created Aug 25, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class DateTerm extends AbstractTerm {
    private static SimpleDateFormat sdf;

    public DateTerm() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        value = cal.getTime();
    }

    public DateTerm(Date date) {
        value = date;
    }

    public DateTerm(String dateFmt) throws ParseException {
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }

        value = sdf.parse(dateFmt);
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.Term#setValue(java.lang.Object)
     */
    public void setValue(Object val) {
        if (val instanceof Date) {
            value = (Date) val;
        } else if (val instanceof String) {
            try {
                value = sdf.parse((String) val);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
