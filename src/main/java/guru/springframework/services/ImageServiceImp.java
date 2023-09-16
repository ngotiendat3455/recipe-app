package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImp implements ImageService {
    private final RecipeRepository recipeRepository;

    public ImageServiceImp(RecipeRepository recipeRepository) {
        this.recipeRepository = (RecipeRepository) recipeRepository;
    }

    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
        log.debug("received a file");
        try {
            Recipe recipe = recipeRepository.findById(Long.valueOf(recipeId)).get();
            Byte[] bytesObj = new Byte[file.getBytes().length];
            int i = 0;
            for (byte b: file.getBytes()){
                bytesObj[i++] = b;
            }
            recipe.setImage(bytesObj);
            recipeRepository.save(recipe);
        } catch (IOException e){
            //todo handle better
            log.debug("received a error");
            log.error("Error occurred", e);

            e.printStackTrace();
        }
    }
}
