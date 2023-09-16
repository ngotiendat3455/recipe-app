package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
@Controller
public class ImageController {
    private final RecipeService recipeService;
    private final ImageService imageService;

    public ImageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("/recipe/{id}/image")
    public String getImageForm(@PathVariable String id, Model model){
        Recipe recipe = recipeService.findById(Long.valueOf(id));
        model.addAttribute("recipe", recipe);
        return "recipe/imageuploadform";
    }

    @GetMapping("/recipe/{id}/recipeimage")
    public void accessImageResouces(@PathVariable String id, HttpServletResponse response) throws IOException {
        RecipeCommand recipe = recipeService.findByIdCommand(Long.valueOf(id));
        // log.debug("image" + Arrays.toString(recipe.getImage()));
        if(recipe.getImage() != null){
            int length = recipe.getImage().length;
            byte[] bytesArray = new byte[length];
            int i = 0;
            for(Byte wrappedByte : recipe.getImage()){
                bytesArray[i++] = wrappedByte;
            }
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(bytesArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }

    @PostMapping("recipe/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile multipartFile){
        imageService.saveImageFile(Long.valueOf(id), multipartFile);
        return "redirect:/recipe/show/" + id;
    }
}
