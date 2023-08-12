package com.fred.minhasempresas.util.cep;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CepUtil {

    private static final String CEP_LA_API_BASE_URL = "http://cep.la/";

    public static boolean consultarCep(String cep) throws IOException, InterruptedException {
        String apiUrl = CEP_LA_API_BASE_URL + cep;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "text/html")
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String htmlResponse = response.body();
            String uf = extrairUFDoCep(htmlResponse);
            
            // Verifica se a UF é "PR"
            if(uf.equalsIgnoreCase("PR")) {
                return uf.equalsIgnoreCase("PR");
            }else {
            	return (response.statusCode() == 200);
            }
        } else {
            throw new IOException("Failed to retrieve CEP information. HTTP Status: " + response.statusCode());
        }
    }

   
    private static String extrairUFDoCep(String cepValue) {
        Pattern pattern = Pattern.compile("\\s([A-Z]{2})\\s(\\d{5}-\\d{3})");
        Matcher matcher = pattern.matcher(cepValue);

        if (matcher.find()) {
            String uf = matcher.group(1); // Captura a UF (grupo 1)
            String cep = matcher.group(2); // Captura o CEP (grupo 2)

            System.out.println("CEP: " + cep);
            System.out.println("UF: " + uf);

            return uf;
        } else {
            return "";
        }
    }
    
    public static boolean validarCepExiste(String cep) {
        // Remova quaisquer caracteres não numéricos do CEP (como hifens)
        cep = cep.replaceAll("\\D", "");

        // Verifica se o CEP possui exatamente 8 dígitos após a remoção de não numéricos
        if (cep.length() == 8) {
            try {
                // Chama o método consultarCep para verificar se o CEP é válido
                
                System.out.println("Retorno Valida CEP: " + consultarCep(cep));

            	return consultarCep(cep);
                
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return false; // Ocorreu um erro na consulta, assume-se como CEP inválido
            }
        } else {
            return false; // CEP inválido devido ao comprimento incorreto
        }
    }
    
    
}
