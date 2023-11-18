package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageReposiry;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageReposiry productImageReposiry;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()->
                        new DataNotFoundException("Cannot find category with id: " + productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(()->
                        new DataNotFoundException("Cannot find product with id: " + id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        // Lấy danh sách sản phẩm theo trang và giới hạn
        return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(
            long productId,
            ProductDTO productDTO)
            throws Exception
    {
        Product existingProduct = getProductById(productId);
        if(existingProduct != null){
            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(()->
                            new DataNotFoundException("Cannot find category with id: " + productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            existingProduct.setDescription(productDTO.getDescription());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);

    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
    @Override
    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(()->
                        new DataNotFoundException("Cannot find product with id: " + productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // Không cho insert quá 5 ảnh cho 1 sản phẩm
      int size =  productImageReposiry.findByProductId(existingProduct.getId()).size();
      if(size > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
          throw new InvalidParamException("Number of image be <= " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
      }
        return productImageReposiry.save(newProductImage);
    }
}
