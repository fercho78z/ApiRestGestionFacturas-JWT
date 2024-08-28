package com.api.facturas.service;

import com.api.facturas.constantes.FacturaConstantes;
import com.api.facturas.dao.FacturaDAO;
import com.api.facturas.pojo.Facturas;
import com.api.facturas.jwt.JwtFilter;
import com.api.facturas.service.FacturaService;
import com.api.facturas.util.FacturaUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private FacturaDAO facturaDAO;

    
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Dentro del método generar reporte");
        try{
            String fileName;
            if(validateRequestMap(requestMap)){
                if(requestMap.containsKey("isGenerate") && !(Boolean)requestMap.get("isGenerate")){
                    fileName = (String) requestMap.get("uuid");
                }
                else{
                    fileName = FacturaUtils.getUUID();
                    requestMap.put("uuid",fileName);
                    insertarFactura(requestMap);
                }

                /*String data = "Nombre : " + requestMap.get("nombre") + "\nNumero de contacto : " + requestMap.get("numeroContacto") + "" +
                        "\n" + "Email : " + requestMap.get("email") + "\n" + "Metodo de pago : " + requestMap.get("metodoPago");
*/
                
                String data ="[{\"id\":18,\"nombre\":\"Nombre 01\",\"categoria\":\"Coffee\",\"cantidad\":\"1\",\"precio\":120,\"total\":120},{\"id\":19,\"nombre\":\"Nombre 02\",\"categoria\":\"Coffee\",\"cantidad\":\"3\",\"precio\":220,\"total\":120},{\"id\":20,\"nombre\":\"Nombre 03\",\"categoria\":\"Coffee\",\"cantidad\":\"3\",\"precio\":120,\"total\":120},{\"id\":21,\"nombre\":\"Nombre 04\",\"categoria\":\"Coffee\",\"cantidad\":\"1\",\"precio\":120,\"total\":120}]";
                Document document = new Document();
                PdfWriter.getInstance(document,new FileOutputStream(FacturaConstantes.STORE_LOCATION+"\\"+fileName+".pdf"));

                document.open();
                setRectangleInPdf(document);

                Paragraph paragrapHeader = new Paragraph("Gestión de categorias y productos\n",getFont("Header"));
                paragrapHeader.setAlignment(Element.ALIGN_CENTER);
                document.add(paragrapHeader);

                PdfPTable pdfPTable = new PdfPTable(5);
                pdfPTable.setWidthPercentage(100);
                addTableHeader(pdfPTable);
        
                
                JSONArray jsonArray = FacturaUtils.getJsonArrayFromString((String)requestMap.get("productoDetalles"));
                System.out.println("jsonArray.length() : "+jsonArray.length());
                //System.out.println("jsonArray.length() : "+jsonArray.getJSONArray(0).getString(0));	
                for(int i = 0;i < jsonArray.length();i++){
                    addRows(pdfPTable,requestMap);
                }
                document.add(pdfPTable);

                Paragraph footer = new Paragraph("Total : " + requestMap.get("montoTotal") + "\n" +
                        "Gracias por visitarnos, vuelva pronto !!",getFont("Data"));
                document.add(footer);

                document.close();

                return new ResponseEntity<>("{\"uuid\":\""+fileName+"\"}", HttpStatus.OK);
            }
            return FacturaUtils.getResponseEntity("Datos requeridos no encontrados",HttpStatus.BAD_REQUEST);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Facturas>> getFacturas() {
        List<Facturas> facturas = new ArrayList<>();
        if(jwtFilter.isAdmin()){
            facturas = facturaDAO.getFacturas();
        }
        else{
            facturas = facturaDAO.getFacturasByUsername(jwtFilter.getCurrentUser());
        }
        return new ResponseEntity<>(facturas,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("Dentro de getPdf : requestMap{}",requestMap);
        try{
            byte[] bytesArray = new byte[0];
            if(!requestMap.containsKey("uuid") && validateRequestMap(requestMap)){
                return new ResponseEntity<>(bytesArray,HttpStatus.BAD_REQUEST);
            }

            String filePath = FacturaConstantes.STORE_LOCATION+"\\"+(String) requestMap.get("uuid")+".pdf";
            log.info("Imprimiendo la ruta");
            log.info(filePath);

            if(FacturaUtils.isFileExist(filePath)){
                bytesArray = getByteArray(filePath);
                return new ResponseEntity<>(bytesArray,HttpStatus.OK);
            }
            else{
                requestMap.put("isGenerate",false);
                generateReport(requestMap);
                bytesArray = getByteArray(filePath);
                return new ResponseEntity<>(bytesArray,HttpStatus.OK);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteFactura(Integer id) {
        try{
            Optional optional = facturaDAO.findById(id);
            if(!optional.isEmpty()){
                facturaDAO.deleteById(id);
                return FacturaUtils.getResponseEntity("Factura eliminada",HttpStatus.OK);
            }
            return FacturaUtils.getResponseEntity("No existe la factura con ese ID",HttpStatus.NOT_FOUND);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private byte[] getByteArray(String filePath) throws IOException {
        File initialFile = new File(filePath);
        InputStream inputStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return byteArray;
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Dentro de setRectangleInPdf");
        Rectangle rectangle = new Rectangle(577,825,18,15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        document.add(rectangle);
    }

    private Font getFont(String type){
        log.info("Dentro de getFont");
        switch (type){
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void addRows(PdfPTable pdfPTable,Map<String,Object> data){
        log.info("Dentro de addRows");
        pdfPTable.addCell((String)data.get("nombre"));
        pdfPTable.addCell((String)data.get("categoria"));
        pdfPTable.addCell((String)data.get("cantidad"));
        //pdfPTable.addCell(Double.toString((Double)data.get("precio")));
        //pdfPTable.addCell(Double.toString((Double)data.get("total")));
    }

    private void addTableHeader(PdfPTable pdfPTable){
        log.info("Dentro del addTableHeader");
        Stream.of("Nombre","Categoria","Cantidad","Precio","Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell pdfPCell = new PdfPCell();
                    pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    pdfPCell.setBorderWidth(2);
                    pdfPCell.setPhrase(new Phrase(columnTitle));
                    pdfPCell.setBackgroundColor(BaseColor.YELLOW);
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPTable.addCell(pdfPCell);
                });
    }

    private void insertarFactura(Map<String,Object> requestMap){
        try{
            Facturas factura = new Facturas();
            factura.setUuid((String)requestMap.get("uuid"));
            factura.setNombre((String)requestMap.get("nombre"));
            factura.setEmail((String)requestMap.get("email"));
            factura.setNumeroContacto((String)requestMap.get("numeroContacto"));
            factura.setMetodoPago((String) requestMap.get("metodoPago"));
            factura.setTotal(Integer.parseInt((String) requestMap.get("montoTotal")));
            factura.setProductoDetalles((String)requestMap.get("productoDetalles"));
            factura.setCreatedBy(jwtFilter.getCurrentUser());
            facturaDAO.save(factura);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String,Object> requestMap){
        return requestMap.containsKey("nombre") &&
                requestMap.containsKey("numeroContacto") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("metodoPago") &&
                requestMap.containsKey("productoDetalles") &&
                requestMap.containsKey("montoTotal");
    }
}
