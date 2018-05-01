package de.htwg.swqs.shopui.util;

import de.htwg.swqs.cart.utils.ShoppingCartException;
import de.htwg.swqs.cart.utils.ShoppingCartItemWrongQuantityException;
import de.htwg.swqs.catalog.utils.ProductNotFoundException;
import de.htwg.swqs.order.util.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(value = {
      ProductNotFoundException.class,
      ShoppingCartException.class,
      ShoppingCartItemWrongQuantityException.class,
      OrderNotFoundException.class}
  )
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ModelAndView handle(RuntimeException exception) {
    ModelAndView mav = new ModelAndView();
    mav.addObject("message", exception.getMessage());
    mav.addObject("stacktrace", exception.getStackTrace());
    mav.addObject("cause", exception.getCause());
    mav.setViewName("error");

    return mav;
  }
}
