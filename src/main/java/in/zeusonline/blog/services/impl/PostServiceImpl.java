package in.zeusonline.blog.services.impl;

import in.zeusonline.blog.exception.ResourceNotFoundException;
import in.zeusonline.blog.model.Post;
import in.zeusonline.blog.payload.PostDto;
import in.zeusonline.blog.payload.PostResponse;
import in.zeusonline.blog.repository.PostRepository;
import in.zeusonline.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;

    }

    @Override
    public PostDto createPost(PostDto postDto) {

        //Convert DTO to entity
        Post post = mapToEntity(postDto);

        Post newPost = postRepository.save(post);

        //Convert entity to DTO
        PostDto postResponse = mapToDto(newPost);

        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        //create Pageable instance
//        Pageable pageable = PageRequest.of(pageNo, pageSize);  //Only for pagination

//        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy)); //For pagination and sorting
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort); //For pagination and sorting with dynamic asc or desc

        Page<Post> posts = postRepository.findAll(pageable);

        //Get content from page object
        List<Post> listOfPosts = posts.getContent();
        List<PostDto> content = listOfPosts.stream().map(post -> mapToDto(post)).toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages((posts.getTotalPages()));
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatePost = postRepository.save(post);

        return mapToDto(updatePost);
    }

    @Override
    public void deletePostById(long id) {

//        postRepository.deleteById(id);

        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        postRepository.delete(post);
    }

    //Convert Entity to Dto
    private PostDto mapToDto(Post post){
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());

        return mapper.map(post, PostDto.class);
    }

    private Post mapToEntity(PostDto postDto){
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());

        return mapper.map(postDto, Post.class);
    }
}
