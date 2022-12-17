package itacad.aliaksandrkryvapust.myfitapp.manager.api;


import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.ProductDtoOutput;

public interface IProductManager extends IManager<ProductDtoOutput, ProductDtoInput>,
        IManagerUpdate<ProductDtoOutput, ProductDtoInput>, IManagerDelete {
}
