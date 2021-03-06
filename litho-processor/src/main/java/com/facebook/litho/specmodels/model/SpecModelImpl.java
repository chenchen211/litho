/*
 * Copyright (c) 2017-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho.specmodels.model;

import com.facebook.litho.specmodels.internal.ImmutableList;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Simple implementation of {@link SpecModel}.
 */
@Immutable
public final class SpecModelImpl implements SpecModel {
  private static final String SPEC_SUFFIX = "Spec";

  private final String mSpecName;
  private final TypeName mSpecTypeName;
  private final String mComponentName;
  private final TypeName mComponentTypeName;
  private final ClassName mComponentClass;
  private final ImmutableList<DelegateMethodModel> mDelegateMethods;
  private final ImmutableList<EventMethodModel> mEventMethods;
  private final ImmutableList<UpdateStateMethodModel> mUpdateStateMethods;
  private final ImmutableList<PropModel> mProps;
  private final ImmutableList<PropDefaultModel> mPropDefaults;
  private final ImmutableList<TypeVariableName> mTypeVariables;
  private final ImmutableList<StateParamModel> mStateValues;
  private final ImmutableList<InterStageInputParamModel> mInterStageInputs;
  private final ImmutableList<TreePropModel> mTreeProps;
  private final ImmutableList<EventDeclarationModel> mEventDeclarations;
  private final ImmutableList<BuilderMethodModel> mImplicitBuilderMethods;
  private final ImmutableList<RenderDataDiffModel> mDiffs;
  private final String mClassJavadoc;
  private final ImmutableList<PropJavadocModel> mPropJavadocs;
  private final boolean mIsPublic;
  private final boolean mHasInjectedDependencies;
  @Nullable private final DependencyInjectionHelper mDependencyInjectionHelper;
  private final Object mRepresentedObject;

  private SpecModelImpl(
      String qualifiedSpecClassName,
      String componentClassName,
      ClassName componentClass,
      ImmutableList<DelegateMethodModel> delegateMethods,
      ImmutableList<EventMethodModel> eventMethods,
      ImmutableList<UpdateStateMethodModel> updateStateMethods,
      ImmutableList<TypeVariableName> typeVariables,
      ImmutableList<PropDefaultModel> propDefaults,
      ImmutableList<EventDeclarationModel> eventDeclarations,
      ImmutableList<BuilderMethodModel> implicitBuilderMethods,
      String classJavadoc,
      ImmutableList<PropJavadocModel> propJavadocs,
      boolean isPublic,
      @Nullable DependencyInjectionHelper dependencyInjectionHelper,
      Object representedObject) {
    mSpecName = getSpecName(qualifiedSpecClassName);
    mSpecTypeName = ClassName.bestGuess(qualifiedSpecClassName);
    mComponentClass = componentClass;
    mComponentName = getComponentName(componentClassName, qualifiedSpecClassName);
    mComponentTypeName = getComponentTypeName(componentClassName, qualifiedSpecClassName);
    mDelegateMethods = delegateMethods;
    mEventMethods = eventMethods;
    mUpdateStateMethods = updateStateMethods;
    mProps = getProps(delegateMethods, eventMethods, updateStateMethods);
    mPropDefaults = propDefaults;
    mTypeVariables = typeVariables;
    mStateValues = getStateValues(delegateMethods, eventMethods, updateStateMethods);
    mInterStageInputs = getInterStageInputs(delegateMethods, eventMethods, updateStateMethods);
    mTreeProps = getTreeProps(delegateMethods, eventMethods, updateStateMethods);
    mEventDeclarations = eventDeclarations;
    mImplicitBuilderMethods = implicitBuilderMethods;
    mDiffs = getDiffs(delegateMethods);
    mClassJavadoc = classJavadoc;
    mPropJavadocs = propJavadocs;
    mIsPublic = isPublic;
    mHasInjectedDependencies = dependencyInjectionHelper != null;
    mDependencyInjectionHelper = dependencyInjectionHelper;
    mRepresentedObject = representedObject;
  }

  @Override
  public String getSpecName() {
    return mSpecName;
  }

  @Override
  public TypeName getSpecTypeName() {
    return mSpecTypeName;
  }

  @Override
  public String getComponentName() {
    return mComponentName;
  }

  @Override
  public TypeName getComponentTypeName() {
    return mComponentTypeName;
  }

  @Override
  public ImmutableList<DelegateMethodModel> getDelegateMethods() {
    return mDelegateMethods;
  }

  @Override
  public ImmutableList<EventMethodModel> getEventMethods() {
    return mEventMethods;
  }

  @Override
  public ImmutableList<UpdateStateMethodModel> getUpdateStateMethods() {
    return mUpdateStateMethods;
  }

  @Override
  public ImmutableList<PropModel> getProps() {
    return mProps;
  }

  @Override
  public ImmutableList<PropDefaultModel> getPropDefaults() {
    return mPropDefaults;
  }

  @Override
  public ImmutableList<TypeVariableName> getTypeVariables() {
    return mTypeVariables;
  }

  @Override
  public ImmutableList<StateParamModel> getStateValues() {
    return mStateValues;
  }

  @Override
  public ImmutableList<InterStageInputParamModel> getInterStageInputs() {
    return mInterStageInputs;
  }

  @Override
  public ImmutableList<TreePropModel> getTreeProps() {
    return mTreeProps;
  }

  @Override
  public ImmutableList<EventDeclarationModel> getEventDeclarations() {
    return mEventDeclarations;
  }

  @Override
  public ImmutableList<BuilderMethodModel> getExtraBuilderMethods() {
    return mImplicitBuilderMethods;
  }

  @Override
  public ImmutableList<RenderDataDiffModel> getRenderDataDiffs() {
    return mDiffs;
  }

  @Override
  public String getClassJavadoc() {
    return mClassJavadoc;
  }

  @Override
  public ImmutableList<PropJavadocModel> getPropJavadocs() {
    return mPropJavadocs;
  }

  @Override
  public boolean isPublic() {
    return mIsPublic;
  }

  @Override
  public ClassName getContextClass() {
    throw new RuntimeException("Don't delegate to this method!");
  }

  @Override
  public ClassName getComponentClass() {
    return mComponentClass;
  }

  @Override
  public ClassName getStateContainerClass() {
    throw new RuntimeException("Don't delegate to this method!");
  }

  @Override
  public TypeName getUpdateStateInterface() {
    throw new RuntimeException("Don't delegate to this method!");
  }

  @Override
  public String getScopeMethodName() {
    throw new RuntimeException("Don't delegate to this method!");
  }

  @Override
  public boolean isStylingSupported() {
    throw new RuntimeException("Don't delegate to this method!");
  }

  @Override
  public boolean hasInjectedDependencies() {
    return mHasInjectedDependencies;
  }

  @Override
  public boolean shouldCheckIdInIsEquivalentToMethod() {
    throw new RuntimeException("Don't delegate to this method!");
  }

  @Override
  public boolean hasDeepCopy() {
    throw new RuntimeException("Don't delegate to this method!");
  }

  @Nullable
  @Override
  public DependencyInjectionHelper getDependencyInjectionHelper() {
    return mDependencyInjectionHelper;
  }

  @Override
  public Object getRepresentedObject() {
    return mRepresentedObject;
  }

  @Override
  public List<SpecModelValidationError> validate() {
    throw new RuntimeException("Don't delegate to this method!");
  }

  @Override
  public TypeSpec generate() {
    throw new RuntimeException("Don't delegate to this method!");
  }

  private static String getSpecName(String qualifiedSpecClassName) {
    return qualifiedSpecClassName.substring(qualifiedSpecClassName.lastIndexOf('.') + 1);
  }

  private static ClassName getComponentTypeName(
      String componentClassName,
      String qualifiedSpecClassName) {
    final String qualifiedComponentClassName;
    if (componentClassName == null || componentClassName.isEmpty()) {
      qualifiedComponentClassName =
          qualifiedSpecClassName.substring(
              0,
              qualifiedSpecClassName.length() - SPEC_SUFFIX.length());
    } else {
      qualifiedComponentClassName =
          qualifiedSpecClassName.substring(0, qualifiedSpecClassName.lastIndexOf('.') + 1) +
              componentClassName;
    }

    return ClassName.bestGuess(qualifiedComponentClassName);
  }

  private static String getComponentName(String componentClassName, String qualifiedSpecClassName) {
    return getComponentTypeName(componentClassName, qualifiedSpecClassName).simpleName();
  }

  private static ImmutableList<PropModel> getProps(
      ImmutableList<DelegateMethodModel> delegateMethods,
      ImmutableList<EventMethodModel> eventMethods,
      ImmutableList<UpdateStateMethodModel> updateStateMethods) {
    final Set<PropModel> props = new LinkedHashSet<>();
    for (DelegateMethodModel delegateMethod : delegateMethods) {
      for (MethodParamModel param : delegateMethod.methodParams) {
        if (param instanceof PropModel) {
          props.add((PropModel) param);
        }
      }
    }

    for (EventMethodModel eventMethod : eventMethods) {
      for (MethodParamModel param : eventMethod.methodParams) {
        if (param instanceof PropModel) {
          props.add((PropModel) param);
        }
      }
    }

    for (UpdateStateMethodModel updateStateMethod : updateStateMethods) {
      for (MethodParamModel param : updateStateMethod.methodParams) {
        if (param instanceof PropModel) {
          props.add((PropModel) param);
        }
      }
    }

    return ImmutableList.copyOf(new ArrayList<>(props));
  }

  private static ImmutableList<StateParamModel> getStateValues(
      ImmutableList<DelegateMethodModel> delegateMethods,
      ImmutableList<EventMethodModel> eventMethods,
      ImmutableList<UpdateStateMethodModel> updateStateMethods) {
    final Set<StateParamModel> stateValues = new LinkedHashSet<>();
    for (DelegateMethodModel delegateMethod : delegateMethods) {
      for (MethodParamModel param : delegateMethod.methodParams) {
        if (param instanceof StateParamModel) {
          stateValues.add((StateParamModel) param);
        }
      }
    }

    for (EventMethodModel eventMethod : eventMethods) {
      for (MethodParamModel param : eventMethod.methodParams) {
        if (param instanceof StateParamModel) {
          stateValues.add((StateParamModel) param);
        }
      }
    }

    for (UpdateStateMethodModel updateStateMethod : updateStateMethods) {
      for (MethodParamModel param : updateStateMethod.methodParams) {
        if (param instanceof StateParamModel) {
          stateValues.add((StateParamModel) param);
        }
      }
    }

    return ImmutableList.copyOf(new ArrayList<>(stateValues));
  }

  private static ImmutableList<RenderDataDiffModel> getDiffs(
      ImmutableList<DelegateMethodModel> delegateMethods) {
    final Set<RenderDataDiffModel> diffs = new LinkedHashSet<>();
    for (DelegateMethodModel delegateMethod : delegateMethods) {
      for (MethodParamModel param : delegateMethod.methodParams) {
        if (param instanceof RenderDataDiffModel) {
          diffs.add((RenderDataDiffModel) param);
        }
      }
    }

    return ImmutableList.copyOf(new ArrayList<>(diffs));
  }

  private static ImmutableList<InterStageInputParamModel> getInterStageInputs(
      ImmutableList<DelegateMethodModel> delegateMethods,
      ImmutableList<EventMethodModel> eventMethods,
      ImmutableList<UpdateStateMethodModel> updateStateMethods) {
    final Set<InterStageInputParamModel> interStageInputs = new LinkedHashSet<>();
    for (DelegateMethodModel delegateMethod : delegateMethods) {
      for (MethodParamModel param : delegateMethod.methodParams) {
        if (param instanceof InterStageInputParamModel) {
          interStageInputs.add((InterStageInputParamModel) param);
        }
      }
    }

    for (EventMethodModel eventMethod : eventMethods) {
      for (MethodParamModel param : eventMethod.methodParams) {
        if (param instanceof InterStageInputParamModel) {
          interStageInputs.add((InterStageInputParamModel) param);
        }
      }
    }

    for (UpdateStateMethodModel updateStateMethod : updateStateMethods) {
      for (MethodParamModel param : updateStateMethod.methodParams) {
        if (param instanceof InterStageInputParamModel) {
          interStageInputs.add((InterStageInputParamModel) param);
        }
      }
    }

    return ImmutableList.copyOf(new ArrayList<>(interStageInputs));
  }

  private static ImmutableList<TreePropModel> getTreeProps(
      ImmutableList<DelegateMethodModel> delegateMethods,
      ImmutableList<EventMethodModel> eventMethods,
      ImmutableList<UpdateStateMethodModel> updateStateMethods) {
    final Set<TreePropModel> treeProps = new LinkedHashSet<>();
    for (DelegateMethodModel delegateMethod : delegateMethods) {
      for (MethodParamModel param : delegateMethod.methodParams) {
        if (param instanceof TreePropModel) {
          treeProps.add((TreePropModel) param);
        }
      }
    }

    for (EventMethodModel eventMethod : eventMethods) {
      for (MethodParamModel param : eventMethod.methodParams) {
        if (param instanceof TreePropModel) {
          treeProps.add((TreePropModel) param);
        }
      }
    }

    for (UpdateStateMethodModel updateStateMethod : updateStateMethods) {
      for (MethodParamModel param : updateStateMethod.methodParams) {
        if (param instanceof TreePropModel) {
          treeProps.add((TreePropModel) param);
        }
      }
    }

    return ImmutableList.copyOf(new ArrayList<>(treeProps));
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String mQualifiedSpecClassName;
    private String mComponentClassName;
    private ClassName mComponentClass;
    private ImmutableList<DelegateMethodModel> mDelegateMethodModels;
    private ImmutableList<EventMethodModel> mEventMethodModels;
    private ImmutableList<UpdateStateMethodModel> mUpdateStateMethodModels;
    private ImmutableList<TypeVariableName> mTypeVariableNames;
    private ImmutableList<PropDefaultModel> mPropDefaultModels;
    private ImmutableList<EventDeclarationModel> mEventDeclarations;
    private ImmutableList<BuilderMethodModel> mBuilderMethodModels;
    private String mClassJavadoc;
    private ImmutableList<PropJavadocModel> mPropJavadocs;
    private boolean mIsPublic;
    @Nullable private DependencyInjectionHelper mDependencyInjectionHelper;
    private Object mRepresentedObject;

    private Builder() {
    }

    public Builder qualifiedSpecClassName(String qualifiedSpecClassName) {
      mQualifiedSpecClassName = qualifiedSpecClassName;
      return this;
    }

    /**
     * The class name for the generated component. May be unspecified, in which case the spec class
     * name is used to determine the class name for the component.
     */
    public Builder componentClassName(String componentClassName) {
      mComponentClassName = componentClassName;
      return this;
    }

    public Builder componentClass(ClassName componentClass) {
      mComponentClass= componentClass;
      return this;
    }

    public Builder delegateMethods(ImmutableList<DelegateMethodModel> delegateMethodModels) {
      mDelegateMethodModels = delegateMethodModels;
      return this;
    }

    public Builder eventMethods(ImmutableList<EventMethodModel> eventMethodModels) {
      mEventMethodModels = eventMethodModels;
      return this;
    }

    public Builder updateStateMethods(
        ImmutableList<UpdateStateMethodModel> updateStateMethodModels) {
      mUpdateStateMethodModels = updateStateMethodModels;
      return this;
    }

    public Builder typeVariables(ImmutableList<TypeVariableName> typeVariableNames) {
      mTypeVariableNames = typeVariableNames;
      return this;
    }

    public Builder propDefaults(ImmutableList<PropDefaultModel> propDefaultModels) {
      mPropDefaultModels = propDefaultModels;
      return this;
    }

    public Builder eventDeclarations(ImmutableList<EventDeclarationModel> eventDeclarations) {
      mEventDeclarations = eventDeclarations;
      return this;
    }

    public Builder extraBuilderMethods(ImmutableList<BuilderMethodModel> builderMethodModels) {
      mBuilderMethodModels = builderMethodModels;
      return this;
    }

    public Builder classJavadoc(String classJavadoc) {
      mClassJavadoc = classJavadoc;
      return this;
    }

    public Builder propJavadocs(ImmutableList<PropJavadocModel> propJavadocs) {
      mPropJavadocs = propJavadocs;
      return this;
    }

    public Builder isPublic(boolean isPublic) {
      mIsPublic = isPublic;
      return this;
    }

    public Builder dependencyInjectionGenerator(
        @Nullable DependencyInjectionHelper dependencyInjectionHelper) {
      mDependencyInjectionHelper = dependencyInjectionHelper;
      return this;
    }

    public Builder representedObject(Object representedObject) {
      mRepresentedObject = representedObject;
      return this;
    }

    public SpecModelImpl build() {
      validate();
      initFieldsIfNecessary();

      return new SpecModelImpl(
          mQualifiedSpecClassName,
          mComponentClassName,
          mComponentClass,
          mDelegateMethodModels,
          mEventMethodModels,
          mUpdateStateMethodModels,
          mTypeVariableNames,
          mPropDefaultModels,
          mEventDeclarations,
          mBuilderMethodModels,
          mClassJavadoc,
          mPropJavadocs,
          mIsPublic,
          mDependencyInjectionHelper,
          mRepresentedObject);
    }

    private void validate() {
      if (mQualifiedSpecClassName == null) {
        throw new IllegalStateException("Must specify a qualified class name");
      }

      if (mDelegateMethodModels == null) {
        throw new IllegalStateException("Must specify delegate methods");
      }

      if (mRepresentedObject == null) {
        throw new IllegalStateException("Must specify represented object");
      }
    }

    private void initFieldsIfNecessary() {
      if (mTypeVariableNames == null) {
        mTypeVariableNames = ImmutableList.of();
      }

      if (mPropDefaultModels == null) {
        mPropDefaultModels = ImmutableList.of();
      }

      if (mEventMethodModels == null) {
        mEventMethodModels = ImmutableList.of();
      }

      if (mUpdateStateMethodModels == null) {
        mUpdateStateMethodModels = ImmutableList.of();
      }

      if (mEventDeclarations == null) {
        mEventDeclarations = ImmutableList.of();
      }
    }
  }
}
