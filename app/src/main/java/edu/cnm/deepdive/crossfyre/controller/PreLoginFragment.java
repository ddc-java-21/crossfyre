package edu.cnm.deepdive.crossfyre.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.crossfyre.R;
import edu.cnm.deepdive.crossfyre.viewmodel.LoginViewModel;

@AndroidEntryPoint
public class PreLoginFragment extends Fragment {

  private View root;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    root = inflater.inflate(R.layout.fragment_pre_login, container, false);
    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    LoginViewModel viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel
        .getAccount()
        .observe(owner, (account) -> {
          if (account != null) {
            Navigation.findNavController(root)
                .navigate(PreLoginFragmentDirections.showList());
          }
        });
    viewModel
        .getRefreshThrowable()
        .observe(owner, (throwable) -> {
          if (throwable != null) {
            Navigation.findNavController(root)
                .navigate(PreLoginFragmentDirections.showLogin());
          }
        });
    viewModel.refresh();
  }

  @Override
  public void onDestroyView() {
    root = null;
    super.onDestroyView();
  }

}
