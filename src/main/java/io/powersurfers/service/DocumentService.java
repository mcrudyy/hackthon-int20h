package io.powersurfers.service;

import io.powersurfers.data.DocumentRepo;
import io.powersurfers.model.User;
import io.powersurfers.model.document.Document;
import io.powersurfers.model.document.Section;
import io.powersurfers.model.response.DocumentResponse;
import io.powersurfers.util.SectionsRestUrlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class DocumentService {

    private DocumentRepo documentRepo;
    private UserService userService;

    @Autowired
    public DocumentService(DocumentRepo documentRepo, UserService userService) {
        log.debug("DocService Run");
        this.documentRepo = documentRepo;
        this.userService = userService;
    }

    public Document getDocumentById(String id) {
        log.debug("getDocById: " + id);
        return documentRepo.findById(id).orElse(null);
    }

    public Document addDocument(Document document) {
        return documentRepo.save(document);
    }

    public List<Document> getDocumentsByOwnerEmail(String email) {
        User user = userService.getUserByEmail(email);
        log.debug("getDocByOwner: " + user);
        return documentRepo.findByOwner(user);
    }

    public Document getDocumentBySectionContains(Section section){
        return documentRepo.findBySectionsContaining(section);
    }

    public DocumentResponse convertToResponse(Document document) {
        if (document == null) return null;
        DocumentResponse response = new DocumentResponse();
        response.setId(document.getId());
        response.setTitle(document.getTitle());
        response.setOwner(document.getOwner());
        response.setModificationAt(document.getModificationAt());

        List<DocumentResponse.SectionResponse> sectionResponseList = new LinkedList<>();

        for (Section section : document.getSections()) {
            DocumentResponse.SectionResponse response1 = new DocumentResponse.SectionResponse();

            response1.setTitle(section.getTitle());
            response1.setNumber(section.getNumber());
            response1.setHref(SectionsRestUrlBuilder.get(section.getId()));
            response1.setStatus(section.getStatus());

            sectionResponseList.add(response1);
        }
        response.setSectionHrefs(sectionResponseList);
        return response;
    }
}
