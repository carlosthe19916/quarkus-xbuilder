/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.quarkus.xbuilder.it;

import io.github.project.openubl.quarkus.xbuilder.XBuilder;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog1_Invoice;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.general.CreditNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocuments;
import io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocumentsItem;
import io.github.project.openubl.xbuilder.enricher.ContentEnricher;
import io.quarkus.qute.Template;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.time.LocalDate;

import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.CREDIT_NOTE;
import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.DEBIT_NOTE;
import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.INVOICE;
import static io.github.project.openubl.quarkus.xbuilder.XBuilder.Type.VOIDED_DOCUMENTS;

@Path("/quarkus-xbuilder")
@ApplicationScoped
public class QuarkusXbuilderResource {

    @Inject
    XBuilder xBuilder;

    @GET
    @Path("invoice")
    public String createInvoice() {
        Invoice invoice = getBaseInvoice();

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(invoice);

        Template template = xBuilder.getTemplate(INVOICE);
        return template.data(invoice).render();
    }

    @GET
    @Path("credit-note")
    public String createCreditNote() {
        CreditNote creditNote = getBaseCreditNote();

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(creditNote);

        Template template = xBuilder.getTemplate(CREDIT_NOTE);
        return template.data(creditNote).render();
    }

    @GET
    @Path("debit-note")
    public String createDebitNote() {
        DebitNote debitNote = getBaseDebitNote();

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(debitNote);

        Template template = xBuilder.getTemplate(DEBIT_NOTE);
        return template.data(debitNote).render();
    }

    @GET
    @Path("voided-documents")
    public String createVoidedDocuments() {
        VoidedDocuments voidedDocuments = getVoidedDocuments();

        ContentEnricher enricher = new ContentEnricher(xBuilder.getDefaults(), () -> LocalDate.of(2022, 1, 25));
        enricher.enrich(voidedDocuments);

        Template template = xBuilder.getTemplate(VOIDED_DOCUMENTS);
        return template.data(voidedDocuments).render();
    }

    private Invoice getBaseInvoice() {
        return Invoice
                .builder()
                .serie("F001")
                .numero(1)
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("10"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .build();
    }

    private CreditNote getBaseCreditNote() {
        return CreditNote
                .builder()
                .serie("FC01")
                .numero(1)
                .comprobanteAfectadoSerieNumero("F001-1")
                .sustentoDescripcion("mi sustento")
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .cliente(Cliente
                        .builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("10"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .build();
    }

    private DebitNote getBaseDebitNote() {
        return DebitNote
                .builder()
                .serie("FD01")
                .numero(1)
                .comprobanteAfectadoSerieNumero("F001-1")
                .sustentoDescripcion("mi sustento")
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .cliente(Cliente.builder()
                        .nombre("Carlos Feria")
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad(Catalog6.RUC.toString())
                        .build()
                )
                .detalle(DocumentoVentaDetalle.builder()
                        .descripcion("Item1")
                        .cantidad(new BigDecimal("10"))
                        .precio(new BigDecimal("100"))
                        .build()
                )
                .build();
    }

    public VoidedDocuments getVoidedDocuments() {
        return VoidedDocuments.builder()
                .numero(1)
                .fechaEmision(LocalDate.of(2022, 01, 31))
                .fechaEmisionComprobantes(LocalDate.of(2022, 01, 29))
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build()
                )
                .comprobante(VoidedDocumentsItem.builder()
                        .serie("F001")
                        .numero(1)
                        .tipoComprobante(Catalog1_Invoice.FACTURA.getCode())
                        .descripcionSustento("Mi sustento1")
                        .build()
                )
                .comprobante(VoidedDocumentsItem.builder()
                        .serie("F001")
                        .numero(2)
                        .tipoComprobante(Catalog1_Invoice.FACTURA.getCode())
                        .descripcionSustento("Mi sustento2")
                        .build()
                )
                .build();
    }
}
