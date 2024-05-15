package com.voghbum.androidaudioprojectreactive.router;

import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentRouter {
    public static final String STUDENT_ROUTE = "/v1/student";
    public static final String STUDENT_COURSES_ROUTE = "/courses";
    private final BookFileHandler studentHandler;

    public StudentRouter(BookFileHandler studentHandler) {
        this.studentHandler = studentHandler;
    }

//    @Bean
//    @RouterOperations(
//            @RouterOperation(
//                    path = STUDENT_COURSES_ROUTE,
//                    method = RequestMethod.GET,
//                    operation =
//                        @Operation(
//                                operationId = "findAllStudentWithCourses",
//                                summary = "Find all students with courses",
//                                description = "Find all students with courses",
//                                responses =
//                                        @ApiResponse(
//                                                responseCode = "200",
//                                                description = "All students with courses",
//                                                content = @Content(schema = @Schema(implementation = BookFileAllDTO.class))
//                                        )
//                        )
//            )
//    )
//    public RouterFunction<ServerResponse> findAllStudentWithCourses() {
//        return RouterFunctions.nest(
//                path(STUDENT_ROUTE),
//                RouterFunctions.route(
//                        GET(STUDENT_COURSES_ROUTE).and(accept(MediaType.APPLICATION_JSON)),
//                        studentHandler::handleFindAllStudentWithCourses)
//        );
//    }
}
