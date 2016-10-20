/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.resolve.calls.model

import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.resolve.calls.components.CheckArguments
import org.jetbrains.kotlin.resolve.calls.components.LambdaAnalyzer
import org.jetbrains.kotlin.resolve.calls.components.*
import org.jetbrains.kotlin.resolve.calls.inference.components.ConstraintInjector
import org.jetbrains.kotlin.resolve.calls.inference.components.ResultTypeResolver
import org.jetbrains.kotlin.resolve.calls.tasks.ExplicitReceiverKind
import org.jetbrains.kotlin.resolve.calls.tower.CandidateFactory
import org.jetbrains.kotlin.resolve.calls.tower.CandidateWithBoundDispatchReceiver
import org.jetbrains.kotlin.resolve.calls.tower.ImplicitScopeTower
import org.jetbrains.kotlin.resolve.scopes.receivers.ReceiverValueWithSmartCastInfo
import org.jetbrains.kotlin.types.ErrorUtils
import org.jetbrains.kotlin.types.TypeSubstitutor


class KotlinCallContext(
        val scopeTower: ImplicitScopeTower,
        val lambdaAnalyzer: LambdaAnalyzer,
        val argumentsToParametersMapper: ArgumentsToParametersMapper,
        val typeArgumentsToParametersMapper: TypeArgumentsToParametersMapper,
        val resultTypeResolver: ResultTypeResolver,
        val callableReferenceResolver: CallableReferenceResolver,
        val constraintInjector: ConstraintInjector
)

class SimpleCandidateFactory(val callContext: KotlinCallContext, val kotlinCall: KotlinCall): CandidateFactory<SimpleKotlinResolutionCandidate> {

    // todo: try something else, because current method is ugly and unstable
    private fun createReceiverArgument(
            explicitReceiver: ReceiverKotlinCallArgument?,
            fromResolution: ReceiverValueWithSmartCastInfo?
    ): SimpleKotlinCallArgument? =
            explicitReceiver as? SimpleKotlinCallArgument ?: // qualifier receiver cannot be safe
            fromResolution?.let { ReceiverExpressionKotlinCallArgument(it, isSafeCall = false) } // todo smartcast implicit this

    override fun createCandidate(
            towerCandidate: CandidateWithBoundDispatchReceiver,
            explicitReceiverKind: ExplicitReceiverKind,
            extensionReceiver: ReceiverValueWithSmartCastInfo?
    ): SimpleKotlinResolutionCandidate {
        val dispatchArgumentReceiver = createReceiverArgument(kotlinCall.getExplicitDispatchReceiver(explicitReceiverKind),
                                                              towerCandidate.dispatchReceiver)
        val extensionArgumentReceiver = createReceiverArgument(kotlinCall.getExplicitExtensionReceiver(explicitReceiverKind), extensionReceiver)

        if (ErrorUtils.isError(towerCandidate.descriptor)) {
            return ErrorKotlinResolutionCandidate(callContext, kotlinCall, explicitReceiverKind, dispatchArgumentReceiver, extensionArgumentReceiver, towerCandidate.descriptor)
        }

        return SimpleKotlinResolutionCandidate(callContext, kotlinCall, explicitReceiverKind, dispatchArgumentReceiver, extensionArgumentReceiver,
                                               towerCandidate.descriptor, towerCandidate.diagnostics)
    }
}

enum class KotlinCallKind(vararg resolutionPart: ResolutionPart) {
    VARIABLE(
            CheckVisibility,
            CheckInfixResolutionPart,
            CheckOperatorResolutionPart,
            NoTypeArguments,
            NoArguments,
            CreteDescriptorWithFreshTypeVariables,
            CheckExplicitReceiverKindConsistency,
            CheckReceivers
    ),
    FUNCTION(
            CheckInstantiationOfAbstractClass,
            CheckVisibility,
            MapTypeArguments,
            MapArguments,
            CreteDescriptorWithFreshTypeVariables,
            CheckExplicitReceiverKindConsistency,
            CheckReceivers,
            CheckArguments
    ),
    UNSUPPORTED();

    val resolutionSequence = resolutionPart.asList()
}

class GivenCandidate(
        val descriptor: FunctionDescriptor,
        val dispatchReceiver: ReceiverValueWithSmartCastInfo?,
        val knownTypeParametersResultingSubstitutor: TypeSubstitutor?
)

