package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public class Main {

    public static boolean esteTerminal(String terminal, String[] terminale) {
        boolean eTerminal = false;
        for (int i = 0; i < terminale.length && !eTerminal; i++) {
            if (terminal.equals(terminale[i])) {
                eTerminal = true;
            }
        }
        return eTerminal;
    }

    public static boolean esteDerivarePosibila(String sirDerivat, String[] terminale)
    {
        if(sirDerivat.length()>60)
            return false;
        else
            for(int i = 0; i<sirDerivat.length(); i++)
            {
                String caracter = Character.toString(sirDerivat.charAt(i));
                if(!esteTerminal(caracter, terminale))
                {
                    return true;//daca s-a gasit cel putin un neterminal, derivarea mai e posibila
                }
            }
        return false;//daca nu s-a gasit niciun neterminal, derivarea nu mai e posibila
    }

    public static String derivareStanga(String sirInitial, String[] terminale, HashMap<String, List<String>> mapProductii)
    {
        String sirDerivareStanga="";
        String sirStanga;
        String sirDreapta;
        boolean aFostDerivare = false;
        for(int i = 0; i<sirInitial.length() && !aFostDerivare; i++)
        {
            String auxiliar = Character.toString(sirInitial.charAt(i));
            if(!esteTerminal(auxiliar, terminale))
            {
                String productieAleasa = alegereProductie(auxiliar, mapProductii);
                sirStanga = sirInitial.substring(0,i);
                sirDreapta = sirInitial.substring(i+1);
                sirDerivareStanga += sirStanga;
                sirDerivareStanga += productieAleasa;
                sirDerivareStanga += sirDreapta;
                aFostDerivare = true;
            }
        }
        return sirDerivareStanga;
    }

    public static String alegereProductie(String neterminal, HashMap<String, List<String>> mapProductii)
    {
        int nrProductiiTotalePentruNeterminal = mapProductii.get(neterminal).size();
        Random random = new Random();
        int numarRandom = random.nextInt(nrProductiiTotalePentruNeterminal);
        return mapProductii.get(neterminal).get(numarRandom);
    }
    public static void main(String[] args) {
        FileInputStream f = null;
        try {
            f = new FileInputStream("gramatica.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        Scanner sc = new Scanner(f);
        List<String> listaProductii = new ArrayList<>();
        String simbolStart;
        HashMap<String, List<String>> mapProductii = new HashMap<>();
        String auxiliarNeterminale= sc.nextLine();
        String[] neterminale = auxiliarNeterminale.split(" ", -1);
        String auxiliarTerminale = sc.nextLine();
        String[] terminale = auxiliarTerminale.split(" ", -1);
        simbolStart = sc.nextLine();
        //citire reguli de derivare din fisier
        while (sc.hasNext())
        {
            String productie = sc.nextLine();
            listaProductii.add(productie);
        }

        //construire map in functie de fiecare neterminal si productiile sale

        for(String productie:listaProductii)
        {
            List<String> listaAuxiliaraProductii = new ArrayList<>();
            String[] splitTerminalsiProductii = productie.split("->",2);
            String[] auxiliarProductii = splitTerminalsiProductii[1].split("\\|", -1);
            for(String splitProductie: auxiliarProductii)
            {
                listaAuxiliaraProductii.add(splitProductie);
            }
            mapProductii.put(splitTerminalsiProductii[0], listaAuxiliaraProductii);
        }
        sc.close();

        String sirDerivat = simbolStart;
        String sirFinal = "";
        //System.out.print(sirDerivat);
        while (esteDerivarePosibila(sirDerivat,terminale))
        {
            sirDerivat = derivareStanga(sirDerivat,terminale, mapProductii);
            //System.out.println("->"+sirDerivat);
            //System.out.println(sirDerivat);
            sirFinal = sirDerivat;
        }
        System.out.println("Sirul final este: "+sirFinal);
    }
}