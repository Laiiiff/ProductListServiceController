package com.example.unicam;

import com.example.unicam.Exception.ProductNotFoundException;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin (origins = "http://localhost:63342")
@RestController

public class ProductServiceController {
    private static Map<String, Product> productRepo = new HashMap<>();
    private ProductListRepository productRepository;
    //autowired mi crea automaticamente il new ProductListRepository
    @Autowired
    public ProductServiceController(ProductListRepository productRepository) {
        this.productRepository = productRepository;
        Product miele = new Product ();
        miele.setId ("1");
        miele.setNome ("Miele");
        //productRepo.put (miele.getId (), miele);
        productRepository.save (miele);

        Product zucchero = new Product ();
        zucchero.setId ("2");
        zucchero.setNome ("Zucchero");
        //productRepo.put (zucchero.getId (), zucchero);
        productRepository.save (zucchero);
    }

    @RequestMapping("/products")
    public ResponseEntity<Object> getProducts() {
        //se metto httpstatus.error mi restituisce errore 404
        return new ResponseEntity<>(productRepository.findAll (), HttpStatus.OK);
    }

    //GetProduct tramite annotazione
    @GetMapping("/product/{id}")

    //Restituisce il prodotto con l'id specificato da cercare
    //@PathVariable("id") restituisce a @GetMapping l'id da ricercare in {id}
    //--@PathParam anche dovrebbe funzionare, ma non durante questa lezione (?)

    public ResponseEntity<Object> getProduct(@PathVariable("id") String id) {
        return new ResponseEntity<>(productRepository.findById (id), HttpStatus.OK);
    }

    //Invece di passare il parametro nell'URL, lo passo tramite il metodo getProductnew
    //Restitusice ?nomeParametro = valoreParametro
    @GetMapping("/product")
    public ResponseEntity<Object> getProductNew(@PathParam("id") String id) {
        return new ResponseEntity<>(productRepository.findById (id), HttpStatus.OK);
    }

    //@PostMapping è come getMapping, ma definita per il posting di articoli
    @PostMapping("/product")
    //@RequestBody (body contenuto della chiamata)
    //prende il parametro per @PostMapping dai parametri di addProduct
    public ResponseEntity<Object> addProduct(@RequestBody Product product) {
        if (!productRepository.existsById (product.getId ())) {
            productRepo.put(product.getId(), product);
            productRepository.save (product);
            return new ResponseEntity<>("Product created", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product already exists", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") String id) {
        //Corretto come controllo, ma si fa nello strato di servizio
        //if(!productRepo.containsKey(id)) {
        //    return new ResponseEntity<>("Product doesn't exist",HttpStatus.NOT_FOUND);
        //}
        productRepo.remove(id);
        productRepository.deleteById (id);
        return new ResponseEntity<>("Product succesfully deleted", HttpStatus.OK);
    }

    @RequestMapping(value = "/product", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateProduct(@PathParam("id") String id, @RequestBody Product product) {
        if (productRepository.existsById (id)) {
            //productRepo.replace(id, product);
            productRepository.save (product);
            productRepo.get(id).setNome(product.getName());
            return new ResponseEntity<>("Product updated", HttpStatus.OK);
        } else throw new ProductNotFoundException();
    }
    //Abbiamo visto come aggiornare oggetti nel sito web
    //Ora vedremo come fare l'upload di file nel servizio web, come una scheda tecnica del prodotto

    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename) throws IOException {
        File newFile = new File("C:\\Users\\Utente\\Desktop\\IDSProjects\\unicam\\src\\main\\resources\\" + filename);
        newFile.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(newFile);
        fileOut.write(file.getBytes());
        fileOut.close();
        return new ResponseEntity<>("File saved", HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<Object> fileDownload(@PathParam("filename") String filename) throws FileNotFoundException {
        String filePath = "C:\\Users\\Utente\\Desktop\\IDSProjects\\unicam\\src\\main\\resources\\" + filename;
        File file = new File(filePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        header.add("Cache-Control", "no-cache, no store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        ResponseEntity<Object> response = ResponseEntity.ok().headers(header).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
        return response;
    }

}


