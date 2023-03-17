package py.com.kyron.sgp.bussines.application.utils;

public class VerifierDigitChecker {

	public VerifierDigitChecker() {
		// TODO Auto-generated constructor stub
	}

    /**
     * Metodo para calcular el Digito Verificador (DV) del RUC, se envia como
     * parametro el numero de cedula y retorna el numero de Ruc.
     *
     * @param numero
     * @return
     */
    public static String getRUC_DV(String numero) {
        int dv = Calcular_Dv_11(numero.trim(), 11);
        return numero.trim() + "-" + dv;
    }

    /**
     * Metodo que retorna el Digito Verificador de cualquier Nro de Cédula.
     *
     * @param numero
     * @return
     */
    public static int getDigitoVerificador(String numero) {
        return Calcular_Dv_11(numero.trim(), 11);
    }

    /**
     * Metodo que verifica si es correcto el digito verificador que se mensiona
     * en el RUC
     *
     * @param ruc
     * @param dv
     * @return
     */
    public static boolean isDigitoVerificador(String ruc, int dv) {
        return getDigitoVerificador(ruc) == dv;
    }

    public static boolean isDigitoVerificador(String doc) {
        if (doc.contains("-")) {
            int guion = doc.indexOf("-");
            String ruc = doc.substring(0, guion);
            int dv = Integer.parseInt(doc.substring(guion + 1));
            return isDigitoVerificador(ruc, dv);
        } else {
            return false;
        }
    }
    
    /**
     * Metodo para calcular el Digito Verificador del Ruc.
     * <>La variable P_BASEMAX que es el segundo parametro se la base.
     * <>En el caso de Paraguay tenemos que usar el 11
     *
     * @param p_numero
     * @param p_basemax
     * @return
     */
    private static int Calcular_Dv_11(String p_numero, int p_basemax) {
        int v_total, v_resto, k, v_numero_aux, v_digit;
        String v_numero_al = "";
        for (int i = 0; i < p_numero.length(); i++) {
            char c = p_numero.charAt(i);
            if (Character.isDigit(c)) {
                v_numero_al += c;
            } else {
                v_numero_al += (int) c;
            }
        }
        k = 2;
        v_total = 0;
        for (int i = v_numero_al.length() - 1; i >= 0; i--) {
            k = k > p_basemax ? 2 : k;
            v_numero_aux = v_numero_al.charAt(i) - 48;
            v_total += v_numero_aux * k++;
        }
        v_resto = v_total % 11;
        v_digit = v_resto > 1 ? 11 - v_resto : 0;

        return v_digit;
}
}
