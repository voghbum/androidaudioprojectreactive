package com.voghbum.androidaudioprojectreactive.router;

import com.voghbum.androidaudioprojectreactive.service.BookFileService;
import org.springframework.stereotype.Component;

@Component
public class BookFileHandler {
    private final BookFileService bookFileService;

    public BookFileHandler(BookFileService bookFileService) {
        this.bookFileService = bookFileService;
    }

//    public Mono<ServerResponse> handleFindAllStudentWithCourses(ServerRequest serverRequest) {
//        return studentService.findAllWithCourses()
//                .flatMap(s -> ServerResponse.ok().bodyValue(s))
//                .switchIfEmpty(
//                        Mono.defer(() -> Mono.error(new RuntimeException("No students found"))));
//    }

}
