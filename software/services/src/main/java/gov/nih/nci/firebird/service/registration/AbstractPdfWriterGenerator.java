/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the NCI OCR Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the NCI OCR Software; (ii) distribute and
 * have distributed to and by third parties the NCI OCR Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM and the National Cancer Institute. If You do not include
 * such end-user documentation, You shall include this acknowledgment in the
 * Software itself, wherever such third-party acknowledgments normally appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", or "5AM"
 * to endorse or promote products derived from this Software. This License does
 * not authorize You to use any trademarks, service marks, trade names, logos or
 * product names of either NCI or 5AM, except as required to comply with the
 * terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC. OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.firebird.service.registration;

import gov.nih.nci.firebird.common.FirebirdDateUtils;
import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.pdf.PdfService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Generates a PDF of an investigator's CV.
 */
@SuppressWarnings("PMD.TooManyMethods")
// more readable when supporting methods are broken down
abstract class AbstractPdfWriterGenerator extends AbstractPdfGenerator {

    static final int ONE_COLUMN = 1;
    static final int TWO_COLUMNS = 2;
    static final int THREE_COLUMNS = 3;
    static final int FOUR_COLUMNS = 4;
    static final int FIVE_COLUMNS = 5;
    static final String SPACE_CHARACTER = " ";
    static final int VALUE_CELL_INDENTATION = 15;
    static final int PADDING_RIGHT = 5;
    private static final int TABLE_SPACING = 10;
    private static final float FIFTY_PERCENT = 50;
    private static final float ONE_HUNDRED_PERCENT = 100;
    static final Font INSTRUCTIONS_HEADER_FONT = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    static final Font INSTRUCTIONS_FONT = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    private static final Font TABLE_HEADER_FONT = new Font(FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
    private static final Font COLUMN_HEADER_FONT = new Font(FontFamily.HELVETICA, 9);
    private static final Font VALUE_FONT = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
    private static final float CELL_PADDING_POINTS = 5;
    private static final BaseColor TABLE_HEADER_COLOR = new BaseColor(82, 128, 162);
    private static final BaseColor TABLE_CELL_COLOR = new BaseColor(239, 244, 247);
    private static final Font TITLE_FONT = new Font(FontFamily.HELVETICA, 16, Font.BOLD, TABLE_HEADER_COLOR);
    private static final Font SECTION_TITLE_FONT = new Font(FontFamily.HELVETICA, 16, Font.BOLD, TABLE_HEADER_COLOR);
    private static final String LOGO_IMAGE_PATH = "/images/ocr_logo_black.png";
    private static final float SPACER_HEIGHT = 2;

    private static final int SIGNATURE_FIELD_WIDTH = 532;
    private static final int SIGNATURE_FIELD_HEIGHT = 48;

    private static final int SIGNATURE_FIELD_LOWER_LEFT_X = 40;
    private static final int SIGNATURE_FIELD_LOWER_LEFT_Y = 40;
    private static final int SIGNATURE_FIELD_UPPER_RIGHT_X = SIGNATURE_FIELD_LOWER_LEFT_X + SIGNATURE_FIELD_WIDTH;
    private static final int SIGNATURE_FIELD_UPPER_RIGHT_Y = SIGNATURE_FIELD_LOWER_LEFT_Y + SIGNATURE_FIELD_HEIGHT;

    private static final float SIGNATURE_FIELD_APPEARANCE_X = 0.5f;
    private static final float SIGNATURE_FIELD_APPEARANCE_Y = 0.5f;
    private static final float SIGNATURE_FIELD_APPEARANCE_WIDTH = SIGNATURE_FIELD_WIDTH - SIGNATURE_FIELD_APPEARANCE_X;
    private static final float SIGNATURE_FIELD_APPEARANCE_HEIGHT = SIGNATURE_FIELD_HEIGHT
            - SIGNATURE_FIELD_APPEARANCE_Y;

    private Document document;
    private final ResourceBundle resources;
    private PdfWriter writer;

    AbstractPdfWriterGenerator(AbstractRegistration registration, PdfService pdfService, FileService fileService,
            ResourceBundle resources) {
        super(registration, pdfService, fileService);
        this.resources = resources;
    }

    void generatePdf(OutputStream pdfOutputStream, boolean includeDateSigned) {
        try {
            openDocument(pdfOutputStream);
            addHeader();
            addBody();
            addFooter();
            document.close();
        } catch (DocumentException e) {
            throw new IllegalStateException("Unexpected exception generating PDF", e);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected exception generating PDF", e);
        }
    }

    private void openDocument(OutputStream pdfOutputStream) throws DocumentException {
        document = new Document(PageSize.LETTER);
        writer = PdfWriter.getInstance(document, pdfOutputStream);
        document.open();
    }

    private void addHeader() throws IOException, DocumentException {
        addLogo();
        addTitle();
        addGenerationDate();
    }

    private void addLogo() throws IOException, DocumentException {
        Image image = Image.getInstance(AbstractPdfWriterGenerator.class.getResource(LOGO_IMAGE_PATH));
        image.setAlignment(Image.RIGHT);
        image.scalePercent(FIFTY_PERCENT);
        document.add(image);
    }

    private void addTitle() throws DocumentException {
        Paragraph title = new Paragraph(getFromResources(getTitleKey()), TITLE_FONT);
        title.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(title);
    }

    Paragraph createSectionTitle(String sectionTitleKey) {
        Paragraph title = new Paragraph(getFromResources(sectionTitleKey), SECTION_TITLE_FONT);
        title.setAlignment(Paragraph.ALIGN_LEFT);
        return title;
    }

    abstract String getTitleKey();

    private void addGenerationDate() throws DocumentException {
        Paragraph generationDateParagraph = new Paragraph();
        generationDateParagraph.setFont(VALUE_FONT);
        generationDateParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        generationDateParagraph.add(getFromResources("pdf.generation.date"));
        generationDateParagraph.add(SPACE_CHARACTER);
        generationDateParagraph.add(formatFullDate(new Date()));
        generationDateParagraph.add(Chunk.NEWLINE);
        generationDateParagraph.add(Chunk.NEWLINE);
        document.add(generationDateParagraph);
    }

    abstract void addBody() throws DocumentException, IOException;

    private void addFooter() throws DocumentException {
        PdfPTable table = createTable(ONE_COLUMN);
        PdfPCell cell = createCell();
        table.setExtendLastRow(true);
        table.addCell(cell);
        document.add(table);
        addSignatureField();
    }

    private void addSignatureField() throws DocumentException {
        PdfFormField field = PdfFormField.createSignature(writer);
        Rectangle signatureFieldRectangle = new Rectangle(SIGNATURE_FIELD_LOWER_LEFT_X, SIGNATURE_FIELD_LOWER_LEFT_Y,
                SIGNATURE_FIELD_UPPER_RIGHT_X, SIGNATURE_FIELD_UPPER_RIGHT_Y);
        field.setWidget(signatureFieldRectangle, PdfAnnotation.HIGHLIGHT_INVERT);
        field.setFieldName("signature");
        field.setFlags(PdfAnnotation.FLAGS_PRINT);
        field.setPage();
        field.setMKBorderColor(BaseColor.BLACK);
        field.setMKBackgroundColor(BaseColor.WHITE);
        PdfAppearance appearance = PdfAppearance
                .createAppearance(writer, SIGNATURE_FIELD_WIDTH, SIGNATURE_FIELD_HEIGHT);
        appearance.rectangle(SIGNATURE_FIELD_APPEARANCE_X, SIGNATURE_FIELD_APPEARANCE_Y,
                SIGNATURE_FIELD_APPEARANCE_WIDTH, SIGNATURE_FIELD_APPEARANCE_HEIGHT);
        appearance.stroke();
        field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, appearance);
        writer.addAnnotation(field);
    }

    PdfPTable createTable(int numberOfColumns) {
        PdfPTable table = new PdfPTable(numberOfColumns);
        table.setWidthPercentage(ONE_HUNDRED_PERCENT);
        table.setSplitRows(false);
        table.setSpacingAfter(TABLE_SPACING);
        table.setKeepTogether(true);
        return table;
    }

    PdfPCell createHeaderCell(String value) {
        Paragraph contents = new Paragraph();
        Chunk chunk = getValueChunk(value);
        chunk.setFont(TABLE_HEADER_FONT);
        contents.add(chunk);
        PdfPCell cell = createCell(contents);
        cell.setBackgroundColor(TABLE_HEADER_COLOR);
        return cell;
    }

    Chunk getValueChunk(String value) {
        return new Chunk(StringUtils.defaultString(value), VALUE_FONT);
    }

    private PdfPCell createCell(Phrase contents) {
        PdfPCell cell = createCell();
        cell.setPhrase(contents);
        return cell;
    }

    PdfPCell createCell() {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(CELL_PADDING_POINTS);
        cell.disableBorderSide(Rectangle.BOX);
        return cell;
    }

    private PdfPCell createTableCell(String value) {
        Paragraph contents = new Paragraph();
        contents.add(getValueChunk(value));
        PdfPCell cell = createTableCell(contents);
        cell.setPaddingLeft(VALUE_CELL_INDENTATION);
        return cell;
    }

    PdfPCell createTableCell(Paragraph contents) {
        PdfPCell cell = createCell(contents);
        cell.setBackgroundColor(TABLE_CELL_COLOR);
        return cell;
    }

    String getFromResources(String key) {
        return resources.getString(key);
    }

    PdfPCell createCell(PdfPTable table) {
        PdfPCell cell = new PdfPCell(table);
        cell.disableBorderSide(Rectangle.BOX);
        return cell;
    }

    void addHeadings(PdfPTable table, int index, String mainHeadingKey, String... columnHeadingKeys) {
        addMainHeaderCell(table, index, mainHeadingKey, columnHeadingKeys.length);
        for (String columnHeadingKey : columnHeadingKeys) {
            addColumnHeadingCell(table, columnHeadingKey);
        }
    }

    private void addMainHeaderCell(PdfPTable table, int index, String mainHeadingKey, int colspan) {
        PdfPCell headerCell = createCell();
        headerCell.setBackgroundColor(TABLE_HEADER_COLOR);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerCell.setColspan(colspan);
        Phrase headerContents = new Phrase(getHeadingChunk(index, mainHeadingKey));
        headerCell.setPhrase(headerContents);
        addToTable(table, headerCell);
    }

    private Chunk getHeadingChunk(int index, String headingKey) {
        return new Chunk(index + ". " + getFromResources(headingKey), TABLE_HEADER_FONT);
    }

    private void addColumnHeadingCell(PdfPTable table, String columnHeadingKey) {
        Paragraph contents = new Paragraph();
        contents.setAlignment(Element.ALIGN_BOTTOM);
        PdfPCell cell = createCell();
        cell.setBackgroundColor(TABLE_CELL_COLOR);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setPhrase(contents);
        contents.add(getColumnHeaderChunk(columnHeadingKey));
        addToTable(table, cell);
    }

    private Chunk getColumnHeaderChunk(String key) {
        return new Chunk(getFromResources(key), COLUMN_HEADER_FONT);
    }

    PdfPCell createHeaderAndValueTableCell(int index, String headerKey, String value, boolean addRightBorder) {
        PdfPTable table = createTable(ONE_COLUMN);
        table.addCell(createHeaderCell(index + ". " + getFromResources(headerKey)));
        table.addCell(createTableCell(value));
        PdfPCell cell = createCell(table);
        if (addRightBorder) {
            cell.setPaddingRight(PADDING_RIGHT);
        }
        return cell;
    }

    void addCellAndCompleteRow(PdfPTable table, String value) {
        PdfPCell cell = createTableCell(value);
        cell.setBackgroundColor(TABLE_CELL_COLOR);
        addCellAndCompleteRow(table, cell);
    }

    private void addCellAndCompleteRow(PdfPTable table, PdfPCell cell) {
        table.addCell(cell);
        for (int i = 0; i < table.getNumberOfColumns() - 1; i++) {
            addToTable(table, new Paragraph());
        }
    }

    private void addToTable(PdfPTable table, Phrase contents) {
        PdfPCell cell = createCell(contents);
        cell.setBackgroundColor(TABLE_CELL_COLOR);
        addToTable(table, cell);
    }

    private void addToTable(PdfPTable table, PdfPCell cell) {
        table.addCell(cell);
    }

    void addValues(PdfPTable table, String... values) {
        for (String value : values) {
            Paragraph contents = new Paragraph();
            contents.add(getValueChunk(value));
            addToTable(table, contents);
        }
    }

    void addSpacerRow() throws DocumentException {
        PdfPTable table = createTable(ONE_COLUMN);
        PdfPCell spacerCell = createCell();
        spacerCell.setFixedHeight(SPACER_HEIGHT);
        table.addCell(spacerCell);
        getDocument().add(table);
    }

    String formatYearDate(Date date) {
        return FirebirdDateUtils.getAsYearOrEmptyString(date);
    }

    String formatMonthAndYearDate(Date date) {
        return FirebirdDateUtils.getAsMonthAndYearOrEmptyString(date);
    }

    private String formatFullDate(Date date) {
        return FirebirdDateUtils.getAsMonthDateAndYearOrEmptyString(date);
    }

    String getValueNone() {
        return getFromResources("cv.value.none");
    }

    Document getDocument() {
        return document;
    }

    @Override
    Collection<FirebirdFile> getAttachments() {
        return Collections.emptySet();
    }

}
