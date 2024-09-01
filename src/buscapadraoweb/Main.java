package buscapadraoweb;

import buscaweb.CapturaRecursosWeb;
import java.util.ArrayList;

public class Main {

    // busca char em vetor e retorna indice
    public static int get_char_ref(char[] vet, char ref) {
        for (int i = 0; i < vet.length; i++) {
            if (vet[i] == ref) {
                return i;
            }
        }
        return -1;
    }

    // busca string em vetor e retorna indice
    public static int get_string_ref(String[] vet, String ref) {
        for (int i = 0; i < vet.length; i++) {
            if (vet[i].equals(ref)) {
                return i;
            }
        }
        return -1;
    }

    // retorna o próximo estado, dado o estado atual e o símbolo lido
    public static int proximo_estado(char[] alfabeto, int[][] matriz, int estado_atual, char simbolo) {
        int simbol_indice = get_char_ref(alfabeto, simbolo);
        if (simbol_indice != -1) {
            return matriz[estado_atual][simbol_indice];
        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        // Instancia e usa objeto que captura código-fonte de páginas Web
        CapturaRecursosWeb crw = new CapturaRecursosWeb();
        
        // Adicione as URLs dos arquivos locais usando o caminho absoluto
        crw.getListaRecursos().add("file:///Users/arthurfelipecarminati/Downloads/BuscaPadraoWeb/pages/page1.html");
        crw.getListaRecursos().add("file:///Users/arthurfelipecarminati/Downloads/BuscaPadraoWeb/pages/page2.html");
        crw.getListaRecursos().add("file:///Users/arthurfelipecarminati/Downloads/BuscaPadraoWeb/pages/page3.html");
    
        // Captura o conteúdo das páginas
        ArrayList<String> listaCodigos = crw.carregarRecursos();
    
        // Verifica se o conteúdo foi carregado
        if (listaCodigos.isEmpty()) {
            System.err.println("Nenhum conteúdo foi carregado das URLs.");
            return; // Sai do programa se não houver conteúdo
        }
    
        // Processa cada página capturada
        for (String codigoHTML : listaCodigos) {
            System.out.println("Conteúdo capturado da página:");
            // System.out.println(codigoHTML); // Exibe o HTML capturado
    
            // Agora, vamos encontrar as placas no conteúdo capturado
            char[] alfabeto = new char[36];
            for (int i = 0; i < 26; i++) {  // Letras A-Z
                alfabeto[i] = (char) ('A' + i);
            }
            for (int i = 0; i < 10; i++) {  // Números 0-9
                alfabeto[26 + i] = (char) ('0' + i);
            }
    
            // Estados do autômato
            String[] estados = {"q0", "q1", "q2", "q3", "q4", "q5", "q6", "q7"};
            String estado_inicial = "q0";
            String[] estados_finais = {"q7"};
    
            // Tabela de transição para reconhecer placas Mercosul
            int[][] matriz = new int[8][36];
    
            // Definir transições para cada estado de acordo com o formato da placa
            for (int i = 0; i < 26; i++) {
                matriz[0][i] = 1;  // q0 -> q1 (letras)
                matriz[1][i] = 2;  // q1 -> q2 (letras)
                matriz[2][i] = 3;  // q2 -> q3 (letras)
                matriz[4][i] = 5;  // q4 -> q5 (letras)
            }
    
            // Transições de números
            for (int i = 26; i < 36; i++) {
                matriz[3][i] = 4;  // q3 -> q4 (números)
                matriz[5][i] = 6;  // q5 -> q6 (números)
                matriz[6][i] = 7;  // q6 -> q7 (números)
            }
    
            int estado = get_string_ref(estados, estado_inicial);
            int estado_anterior = -1;
            ArrayList<String> placas_reconhecidas = new ArrayList<>();
    
            String placa = "";
    
            // Varre o código-fonte de cada página
            for (int i = 0; i < codigoHTML.length(); i++) {
    
                estado_anterior = estado;
                estado = proximo_estado(alfabeto, matriz, estado, codigoHTML.charAt(i));
                if (estado == -1) {
                    estado = get_string_ref(estados, estado_inicial);
                    if (get_string_ref(estados_finais, estados[estado_anterior]) != -1) {
                        if (!placa.equals("")) {
                            placas_reconhecidas.add(placa);
                        }
                        i--;
                    }
                    placa = "";
                } else {
                    placa += codigoHTML.charAt(i);
                }
            }
    
            // Exibe todas as placas reconhecidas
            for (String p : placas_reconhecidas) {
                System.out.println("Placa reconhecida: " + p);
            }
        }
    }
}