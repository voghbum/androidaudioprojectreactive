package com.voghbum.androidaudioprojectreactive.router;

import org.springframework.context.annotation.Configuration;

@Configuration
public class BookFileRouter {
    public static final String BOOK_FILE_ROOT = "/v1/bookFile";
    public static final String BOOK_FILE_URL = "/bookFile";
    private final BookFileHandler studentHandler;

    public BookFileRouter(BookFileHandler studentHandler) {
        this.studentHandler = studentHandler;
    }

//    @Bean
//    @RouterOperations(
//            @RouterOperation(
//                    path = BOOK_FILE_URL,
//                    method = RequestMethod.GET,
//                    operation =
//                        @Operation(
//                                operationId = "",
//                                summary = "",
//                                description = "",
//                                responses =
//                                        @ApiResponse(
//                                                responseCode = "200",
//                                                description = "",
//                                                content = @Content(schema = @Schema(implementation = BookFileAllDTO.class))
//                                        )
//                        )
//            )
//    )
//    public RouterFunction<ServerResponse> findAllStudentWithCourses() {
//        return RouterFunctions.nest(
//                path(BOOK_FILE_ROOT),
//                RouterFunctions.route(
//                        GET(BOOK_FILE_URL).and(accept(MediaType.APPLICATION_JSON)),
//                        studentHandler::handleFindAllStudentWithCourses)
//        );
//    }
}
