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
package gov.nih.nci.firebird.web.common.registration;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Class that is used to handle the movement through the flow of the
 * user's registration process.
 */
//Methods below are necessary for clarity and functionality of a controller.
@SuppressWarnings({ "PMD.TooManyMethods" })
public final class RegistrationFlowController implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final EnumSet<RegistrationFlowStep> INITIAL_ADD_ROLES_STEPS = EnumSet
            .of(RegistrationFlowStep.ROLE_SELECTION);

    private static final EnumSet<RegistrationFlowStep> INITIAL_REGISTRATION_STEPS = EnumSet
            .of(RegistrationFlowStep.VIEW_SELECTED_ROLES);

    private final EnumSet<RegistrationFlowStep> flowBase;
    private List<RegistrationFlowStep> flowSteps = Lists.newArrayList();
    private final EnumSet<RegistrationFlowStep> visitedSteps = EnumSet.noneOf(RegistrationFlowStep.class);
    private RegistrationFlowStep currentStep;

    /**
     * default constructor.
     */
    private RegistrationFlowController(EnumSet<RegistrationFlowStep> steps) {
        flowBase = steps;
        flowSteps.addAll(steps);
        currentStep = flowSteps.get(0);
        visitedSteps.add(currentStep);
    }

    /**
     * Sets the Flow up to use the provided list of Flow Steps. Removes any steps that no longer exist
     * from the Visited steps and resets the currentStep to ROLE_SELECTION as if this were a new flow.
     * Makes sure the beginning step is ROLE_SELECTION and the end step is VERIFICATION.
     *
     * @param newSteps the list of new steps that this flow consists of.
     * @param calledFromStep the current step calling setup.
     */
    public void setupFlowBodyWithSteps(EnumSet<RegistrationFlowStep> newSteps, RegistrationFlowStep calledFromStep) {
        checkInputIsValid(newSteps);
        resetSteps();
        addSteps(newSteps);
        flowSteps.add(RegistrationFlowStep.VERIFICATION);
        currentStep = calledFromStep;
    }

    private void checkInputIsValid(EnumSet<RegistrationFlowStep> newSteps) {
        if (newSteps == null) {
            throw new IllegalArgumentException("Null Steps is not a valid collection to reset to!");
        }
    }

    private void resetSteps() {
        flowSteps = Lists.newArrayList(flowBase);
        visitedSteps.retainAll(flowBase);
    }

    private void addSteps(EnumSet<RegistrationFlowStep> newSteps) {
        for (RegistrationFlowStep step : newSteps) {
            if (!flowSteps.contains(step)) {
                flowSteps.add(step);
            }
        }
    }

    /**
     * Moves the current step state forward.
     * @return the new current step.
     */
    public RegistrationFlowStep stepForward() {
        if (hasNextStep()) {
            currentStep = getNextStep();
            visitedSteps.add(currentStep);
            return currentStep;
        } else {
            return currentStep;
        }
    }

    private RegistrationFlowStep getNextStep() {
        int currentIndex = flowSteps.indexOf(currentStep);
        return flowSteps.get(currentIndex + 1);
    }

    /**
     * Moves the current step state Backward.
     * @return the new current step.
     */
    public RegistrationFlowStep stepBackward() {
        if (hasPreviousStep()) {
            currentStep = getPreviousStep();
            return currentStep;
        } else {
            return currentStep;
        }
    }

    private RegistrationFlowStep getPreviousStep() {
        int currentIndex = flowSteps.indexOf(currentStep);
        return flowSteps.get(currentIndex - 1);
    }

    /**
     * Changes the flow to currently be at the provided step.
     *
     * @param stepTo the Step to make current.
     */
    public void gotoStep(RegistrationFlowStep stepTo) {
        if (stepTo != getCurrentStep()) {
            checkStepIsValid(stepTo);
            checkStepHasBeenVisited(stepTo);
            currentStep = stepTo;
        }
    }

    private void checkStepIsValid(RegistrationFlowStep stepToCheck) {
        if (!flowSteps.contains(stepToCheck)) {
            throw new IllegalArgumentException("Cannot go to a step that is not part of this flow!");
        }
    }

    private void checkStepHasBeenVisited(RegistrationFlowStep stepToCheck) {
        if (!visitedSteps.contains(stepToCheck)) {
            throw new IllegalArgumentException("Cannot go to a step that has not already been visited!");
        }
    }

    /**
     * @return an unmodifiable version of the flow step list.
     */
    public List<RegistrationFlowStep> getFlowSteps() {
        return Collections.unmodifiableList(flowSteps);
    }

    /**
     * @return a copy of the set of visited steps.
     */
    public EnumSet<RegistrationFlowStep> getVisitedSteps() {
        return EnumSet.copyOf(visitedSteps);
    }

    /**
     * @param step Registration step to remove
     * @return True if the step was removed
     */
    public boolean removeVisitedStep(RegistrationFlowStep step) {
        return visitedSteps.remove(step);
    }

    /**
     * @return the current Step in the flow. Useful for display of breadcrumbs.
     */
    public RegistrationFlowStep getCurrentStep() {
        return currentStep;
    }

    /**
     * @return whether there is a next step in the flow or not. Useful for display of button.
     */
    public boolean hasNextStep() {
        return !currentStep.equals(flowSteps.get(flowSteps.size() - 1));
    }

    /**
     * @return whether there is a previous step in the flow or not. Useful for display of button.
     */
    public boolean hasPreviousStep() {
        return !currentStep.equals(flowSteps.get(0));
    }

    /**
     * Factory method to create a basic RegistrationFlowController including just the initial role display step.
     *
     * @return the Flow Controller.
     */
    public static RegistrationFlowController createRegistrationFlow() {
        return new RegistrationFlowController(INITIAL_REGISTRATION_STEPS);
    }

    /**
     * Factory method to create a basic RegistrationFlowController including just the initial role selection step.
     *
     * @return the Flow Controller.
     */
    public static RegistrationFlowController createAddRolesFlow() {
        return new RegistrationFlowController(INITIAL_ADD_ROLES_STEPS);
    }

}
