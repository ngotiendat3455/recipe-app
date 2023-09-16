package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;
    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("/recipe/{id}/ingredients")
    String getList(@PathVariable String id, Model model){
        RecipeCommand recipe = recipeService.findByIdCommand(Long.valueOf(id));
        model.addAttribute("recipe", recipe);
        return "recipe/ingredient/list";
    };

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model){
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId));
        model.addAttribute("ingredient", ingredientCommand);
        return "recipe/ingredient/show";
    };

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    String getIngredientDetail(@PathVariable String recipeId, @PathVariable String ingredientId, Model model){
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId));
        Set<UnitOfMeasureCommand> unitOfMeasureCommand = unitOfMeasureService.listUOM();
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureCommand);
        return "recipe/ingredient/ingredientform";
    };

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    String getIngredientNew(@PathVariable String recipeId, Model model){
        IngredientCommand ingredientCommand = new IngredientCommand();
        Set<UnitOfMeasureCommand> unitOfMeasureCommand = unitOfMeasureService.listUOM();
        ingredientCommand.setRecipeId(Long.valueOf(recipeId));
        ingredientCommand.setUom(new UnitOfMeasureCommand());
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureCommand);
        return "recipe/ingredient/ingredientform";
    };
    @PostMapping("/recipe/{recipeId}/ingredient")
    String postIngredient(@ModelAttribute IngredientCommand ingredientCommand){
        // IngredientCommand ingredientCommand1 = ingredientService.
        IngredientCommand ingredientCommandSaved = ingredientService.saveIngredientCommand(ingredientCommand);
        log.debug("saved receipe id:" + ingredientCommandSaved.getRecipeId());
        log.debug("saved ingredient id:" + ingredientCommandSaved.getId());

        return "redirect:/recipe/" + ingredientCommandSaved.getRecipeId() + "/ingredient/" + ingredientCommandSaved.getId() + "/show";
    };

    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId){
        log.debug("deleting ingredient id:" + recipeId);
        ingredientService.deleteById(Long.valueOf(recipeId), Long.valueOf(ingredientId));
        return "redirect:/recipe/" + recipeId + "/ingredients";
    };
}
