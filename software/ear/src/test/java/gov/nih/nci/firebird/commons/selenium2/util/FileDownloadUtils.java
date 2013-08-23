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
package gov.nih.nci.firebird.commons.selenium2.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Provides methods for downloading files using Selenium 2 WebDriver.
 */
public class FileDownloadUtils {

    private FileDownloadUtils() {
        // Utility class
    }

    /**
     * Clicks a download link and returns the response including headers and the downloaded file. The file will be
     * deleted on exit from the JVM.
     *
     * @param link web element to click to start the download
     * @return the response including the file and the response headers.
     */
    public static FileDownloadResponse clickDownloadLink(WebDriver driver, WebElement link) throws IOException {
        String downloadUri = getDownloadUri(driver, link);
        link.click();
        return downloadFromUri(driver, downloadUri);
    }

    private static String getDownloadUri(WebDriver driver, WebElement link) {
        String href = link.getAttribute("href");
        return URI.create(driver.getCurrentUrl()).resolve(href).toString();
    }

    /**
     * Downloads and returns a file from the given uri using the given query parameters.
     *
     * @param driver the driver
     * @param uri the uri to use to download the file
     * @param queryParameters query parameters
     * @return the response including the file and the response headers.
     */
    public static FileDownloadResponse downloadFromUri(WebDriver driver, String downloadUrl,
            Map<String, String> queryParameters) throws IOException {
        return downloadFromUri(driver, addQueryParametersToUri(downloadUrl, queryParameters));
    }

    private static String addQueryParametersToUri(String uri, Map<String, String> queryParameters) {
        if (uri.contains("?")) {
            return uri + "&" + buildQueryParametersString(queryParameters);
        } else {
            return uri + "?" + buildQueryParametersString(queryParameters);
        }
    }

    private static String buildQueryParametersString(Map<String, String> queryParameters) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!MapUtils.isEmpty(queryParameters)) {
            for (Entry<String, String> entry : queryParameters.entrySet()) {
                stringBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Downloads and returns a file from the given uri.
     *
     * @param driver the driver
     * @param uri the uri to use to download the file
     * @return the response including the file and the response headers.
     */
    public static FileDownloadResponse downloadFromUri(WebDriver driver, String uri) throws IOException {
        File file = getDestinationFile();
        OutputStream out = FileUtils.openOutputStream(file);
        InputStream inputStream = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            setupCookies(client, driver);
            HttpGet get = new HttpGet(uri);
            HttpResponse response = client.execute(get);
            inputStream = response.getEntity().getContent();
            IOUtils.copy(inputStream, out);
            return new FileDownloadResponse(file, response.getAllHeaders());
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(inputStream);
        }
    }

    private static void setupCookies(DefaultHttpClient client, WebDriver driver) {
        CookieStore cookieStore = new BasicCookieStore();
        for (Cookie webDriverCookie : driver.manage().getCookies()) {
            cookieStore.addCookie(translateCookie(webDriverCookie));
        }
        client.setCookieStore(cookieStore);
    }

    private static BasicClientCookie translateCookie(Cookie webDriverCookie) {
        BasicClientCookie clientCookie = new BasicClientCookie(webDriverCookie.getName(), webDriverCookie.getValue());
        clientCookie.setDomain(webDriverCookie.getDomain());
        clientCookie.setExpiryDate(webDriverCookie.getExpiry());
        clientCookie.setPath(webDriverCookie.getPath());
        clientCookie.setSecure(webDriverCookie.isSecure());
        return clientCookie;
    }

    private static File getDestinationFile() throws IOException {
        File file = File.createTempFile("download", null);
        FileUtils.forceDeleteOnExit(file);
        return file;
    }

    /**
     * Encapsulates the headers and content from a file download request.
     */
    public static class FileDownloadResponse {

        private final File file;
        private final Header[] headers;

        private FileDownloadResponse(File file, Header[] headers) {
            this.file = file;
            this.headers = headers;
        }

        /**
         * @return the file
         */
        public File getFile() {
            return file;
        }

        /**
         * @return the headers
         */
        public Header[] getHeaders() {
            return headers;
        }

    }

}